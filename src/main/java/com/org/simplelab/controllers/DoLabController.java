package com.org.simplelab.controllers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.services.projections.Projection;
import com.org.simplelab.restcontrollers.dto.Workspace;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RequestMapping(DoLabController.DO_LAB_BASE_MAPPING)
@RestController
public class DoLabController extends BaseController {
    //TODO: add security

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





}
