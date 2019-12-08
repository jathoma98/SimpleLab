package com.org.simplelab.controllers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.Recipe;
import com.org.simplelab.database.services.projections.Projection;
import com.org.simplelab.game.RecipeHandler;
import com.org.simplelab.restcontrollers.dto.Workspace;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.org.simplelab.restcontrollers.dto.DTO.EquipmentInteractionDTO;

@RequestMapping(DoLabController.DO_LAB_BASE_MAPPING)
@RestController
public class DoLabController extends BaseController {
    //TODO: add security

    @Autowired
    RecipeHandler recipeHandler;

    public static final String DO_LAB_BASE_MAPPING = "/doLab";
    public static final String LAB_ID_MAPPING = "/{lab_id}";
    public static final String INTERACTION_MAPPING  = LAB_ID_MAPPING + "/interaction";

    /**
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
        Workspace ws;
        Lab found = labDB.findById(lab_id);
        System.out.println(found.toString());
        if (found == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }
        ws = DBUtils.getMapper().map(found, Workspace.class);
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
     *     TODO: discuss if the interaction should be sent in the DTO -- requires client to manage interactions
     *     TODO: current plan is to have interactions be defined as Java interfaces on backend
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
     * If the interaction is valid and fulfills a recipe:
     * RRO with: {
     *     success: true
     *     action: MODIFY_EQUIPMENT
     *     data: TODO: discuss this, this should return data for UI to render new objects and delete old ones.
     *           ex: mixing water beaker with oil beaker should create 2 new beakers: empty beaker
     *           and beaker with mixture.
     * }
     *
     * If the interaction is valid and completes the step:
     * RRO with: {
     *     success: true
     *     action: ADVANCE_STEP
     * }
     */
    @PostMapping(INTERACTION_MAPPING)
    public RRO handleEquipmentInteraction(@PathVariable("lab_id") long lab_id,
                                          @RequestBody EquipmentInteractionDTO dto){
        Recipe found = recipeHandler.findRecipe(dto.getObject1(), dto.getObject2());
        if (found.exists()){
            // do something
        }
        return RRO.sendErrorMessage(RRO.MSG.RECIPE_NOT_FOUND.getMsg());
    }






}
