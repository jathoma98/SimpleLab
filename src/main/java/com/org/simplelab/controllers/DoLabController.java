package com.org.simplelab.controllers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.game.DoLabEventHandler;
import com.org.simplelab.game.RecipeHandler;
import com.org.simplelab.restcontrollers.dto.Workspace;
import com.org.simplelab.restcontrollers.rro.RRO;
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
    public static final String INTERACTION_MAPPING  = LAB_ID_MAPPING + "/interaction";

    /**
     * Initiates building of a Workspace document, which will record user actions and state of a lab in progress.
     * Returns a Workspace object, which contains all of the info needed to build
     * the Do Lab user interface. Refer to the Workspace class in restcontrollers.dto.Workspace
     * for the fields that will be returned.
     *
     * Example:
     * User clicks on lab with id 100 to do the lab ->
     * @return: {
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
        Workspace ws = eventHandler.buildWorkspace(found);

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
     * }
     *
     * If the interaction is valid and completes the lab:
     * RRO with: {
     *     success: true
     *     action: COMPLETE_LAB
     *     data: TODO: discuss this, probably just return a grade or something
     *     //TODO: May be just return how time student make invalid recipes - Zee
     * }
     */

    @PostMapping(INTERACTION_MAPPING)
    public RRO handleEquipmentInteraction(@PathVariable("lab_id") long lab_id,
                                          @RequestBody EquipmentInteractionDTO dto){

        String interation = "test interaction";
        RRO<Equipment[]> rro = new RRO<>();

        //TODO: implement this
        //save the interaction to lab history
        historyDB.addToLabHistory(interation);

        Equipment eq1 = dto.getObject1();
        Equipment eq2 = dto.getObject2();
        String parameter = dto.getParameter();


        Equipment result = eq1.getInteraction().interactWith(eq2, parameter);
        if (result.exists()){
            eq2 = result;
        }
        rro.setAction(RRO.LAB_ACTION_TYPE.MODIFY_EQUIPMENT.name());
        rro.setData(new Equipment[]{eq1, eq2});

        Recipe found = recipeHandler.findRecipe(eq1, eq2);
        if (found.exists()){
            // do something
        }

        Lab currentLab = labDB.findById(lab_id);
        Step currentStep =  currentLab.getSteps()
                             .stream()
                             .filter(step -> step.getStepNum() == dto.getStepNum())
                             .collect(Collectors.toList()).get(0);

        //Check if the object is equal to the current target object.
        if (eq2.equals(currentStep.getTargetObject())){
            //if it matches the target object, check if this is the last step
            rro.setData(null);
            if (dto.getStepNum() == currentLab.getLastStepNumber()){
                rro.setAction(RRO.LAB_ACTION_TYPE.COMPLETE_LAB.name());
            } else {
                rro.setAction(RRO.LAB_ACTION_TYPE.ADVANCE_STEP.name());
            }
        }
        rro.setSuccess(true);
        return rro;
    }









}
