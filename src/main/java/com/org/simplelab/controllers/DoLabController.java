package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.game.DoLabEventHandler;
import com.org.simplelab.game.RecipeHandler;
import com.org.simplelab.restcontrollers.dto.Workspace;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.org.simplelab.restcontrollers.dto.DTO.EquipmentInteractionDTO;

@RequestMapping(DoLabController.DO_LAB_BASE_MAPPING)
@RestController
public class DoLabController extends BaseController {
    //TODO: add security

    @Autowired
    RecipeHandler recipeHandler;

    @Autowired
    DoLabEventHandler eventHandler;


    public static final String DO_LAB_BASE_MAPPING = "/doLab";
    public static final String LAB_ID_MAPPING = "/{lab_id}";
    public static final String INTERACTION_MAPPING  = "/{instance_id}";

    /**
     * Initiates building of a LabInstance document, which will record user actions and state of a lab in progress.
     * Returns a Workspace object, which contains all of the info needed to build
     * the Do Lab user interface. Refer to the Workspace class in restcontrollers.dto.Workspace
     * for the fields that will be returned.
     *
     * Example:
     * User clicks on lab with id 100 to do the lab ->
     * @return: {
     *     instance_id: MongoDB id of the lab instance. This should be saved in HTML
     *     name: Name of lab
     *     description : Description of lab
     *     steps: List of Step objects
     *     equipments: List of equipments in the lab
     *     (TODO: add this) recipes: List of Recipes in the lab
     * }
     */
    @GetMapping(LAB_ID_MAPPING)
    @Transactional
    public RRO getLabToDo(@PathVariable("lab_id") long lab_id){
        Lab found = labDB.findById(lab_id);
        if (found == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }
        Workspace ws = eventHandler.buildWorkspace(found, getUserIdFromSession());

        RRO<Workspace> rro = new RRO<>();
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(ws);
        return rro;
    }

    /**
     * Handle interaction between 2 Equipments in the Workspace.
     * Example: User pours one Equipment beaker into another Equipment beaker. A DTO
     * is sent to this endpoint to handle:
     * 1. Check if the interaction is valid
     * 2. Check if the result of the interaction fits a Recipe. If it does, perform the Recipe
     * 3. Check if the result of the interaction (or Recipe) matches the targetObject for the step.
     * DTO format: {
     *     object1: Equipment -- The Equipment that the user is dragging. Ex: user drags beaker to another beaker,
     *                                                                        the beaker the User is holding is object1.
     *     object2: Equipment -- Equipment that the user interacts with.
     *                           In the previous example, the beaker the User clicks on is object2
     *     parameter: String -- Represents parameter for interaction.
     *                          ex: If we want to pour 100ml liquid from beaker 1 to beaker 2, parameter = 100.
     *     stepNum: int -- current Step the user is on.
     * }
     *
     * @return:
     * If the interaction is invalid:
     * RRO with: {
     *     success: false
     *     action: PRINT_MSG
     *     msg: "Invalid action"
     * }
     *
     * If the interaction is valid and fulfills a recipe or completes a valid interaction:
     * RRO with: {
     *     success: true
     *     action: MODIFY_EQUIPMENT
     *     data: Equipment[] with size 2, where:
     *           index 0 = equipment1
     *           index 1 = equipment2, which is the result of the recipe or the interaction. this should replace
     *           the old equipment2 in the UI.
     * }
     *
     * If the interaction is valid and completes the step:
     * RRO with: {
     *     success: true
     *     action: ADVANCE_STEP
     *     data: Equipment[] with size 2 when there is new equipment to load.
     * }
     *
     * If the interaction is valid and completes the lab:
     * RRO with: {
     *     success: true
     *     action: COMPLETE_LAB
     * }
     */

    @PostMapping(INTERACTION_MAPPING)
    public RRO handleEquipmentInteraction(@PathVariable("instance_id") String instance_id,
                                          @RequestBody EquipmentInteractionDTO dto){

        RRO<Equipment[]> rro = new RRO<>();
        /**
         * First, find the LabInstance we created for this lab when we called
         * getLabToDo() method above, which initialized the workspace and lab history.
         */
        LabInstance currentInstance = instanceDB.findById(instance_id);

        /**
         * Load the equipment and parameters which were sent from the client.
         * The parameter determines how the interaction will be performed.
         * ex: if we want use a heater to heat something, the parameter is the increase in temperature.
         */
        Equipment eq1 = dto.getObject1();
        Equipment eq2 = dto.getObject2();
        String parameter = dto.getParameter();

        /**
         * Perform the interaction according to the Interaction interface in eq1, which is loaded
         * based on the "type" value.
         * ex: If an equipment has type "heat", the Heat interface is loaded automatically when we
         * pull from DB and performed here.
         */
        Equipment eq2_original = eq2;
        Equipment result = eq1.getInteraction().interactWith(eq2, parameter);
        if (result.exists()){
            eq2 = result;
        }

        /**
         * After we perform the interaction, save the info about the interaction in MongoDB.
         * Each step has a StepRecord object in the LabInstance. The StepRecord has a list of
         * StepActions. StepAction records information about every interaction the user makes
         * in each step.
         */
        DoLabEventHandler.InteractionObjects interactionInfo = new DoLabEventHandler.InteractionObjects();
        interactionInfo.setEq1(eq1);
        interactionInfo.setEq2(eq2);
        interactionInfo.setInteraction(eq1.getInteraction());
        interactionInfo.setResult(eq2);
        interactionInfo.setParameter(parameter);
        eventHandler.addInteractionToHistory(currentInstance, dto.getStepNum(), interactionInfo);

        /**
         * Indicate to the UI that equipment was modified, and it should be reloaded.
         */
        rro.setAction(RRO.LAB_ACTION_TYPE.MODIFY_EQUIPMENT.name());
        rro.setData(new Equipment[]{eq1, eq2});

        Recipe found = recipeHandler.findRecipe(eq1, eq2);
        if (found.exists()){
            // do something
        }

        /**
         * Now we load the current step from the Lab that this instance manages.
         */
        Lab currentLab = SerializationUtils.deserialize(currentInstance.getSerialized_lab());
        Step currentStep =  currentLab.getSteps()
                             .stream()
                             .filter(step -> step.getStepNum() == dto.getStepNum())
                             .collect(Collectors.toList()).get(0);

        /**
         * After loading the current step, check if the object that resulted from the
         * interaction is equal to the target object for this step.
         */
        if (eq2.equals(currentStep.getTargetObject())){
            /**
             * If it matches, check if the current step is the last step in the lab.
             */
            if (dto.getStepNum() == currentLab.returnLastStepNumber()){
                /**
                 * If the step is complete and the step is the last step in the lab,
                 * the lab is complete. We finalize the lab by assigning a grade,
                 * and setting the variable finished = true.
                 * RRO returns COMPLETE_LAB to indicate to the UI that the lab is done.
                 */
                eventHandler.finalizeInstance(currentInstance);
                rro.setData(null);
                rro.setAction(RRO.LAB_ACTION_TYPE.COMPLETE_LAB.name());
            } else {
                /**
                 * Otherwise, we create a new StepRecord object and add it to the list
                 * of StepRecords in the LabInstance. This is because we are advancing to the next
                 * step, so we should save future user actions in the object for the next step.
                 * We also set RRO to ADVANCE_STEP to indicate to the UI that we should go to the
                 * next step.
                 */
                eventHandler.advanceStep(currentInstance);
                rro.setAction(RRO.LAB_ACTION_TYPE.ADVANCE_STEP.name());
            }
        }
        rro.setSuccess(true);
        return rro;
    }









}