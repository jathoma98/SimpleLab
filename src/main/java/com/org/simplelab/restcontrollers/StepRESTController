package com.org.simplelab.restcontrollers;


import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.database.services.RecipeDB;
import com.org.simplelab.database.services.StepDB;
import com.org.simplelab.database.validators.InvalidFieldException;
import com.org.simplelab.database.validators.RecipeValidator;
import com.org.simplelab.database.validators.StepValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(RecipeRESTController.BASE_MAPPING)
@Getter
public class StepRESTController extends BaseRESTController<Step> {
    public static final String BASE_MAPPING = "/step/rest";
    public static final String STEP_ID_MAPPING = "/{step_id}";

    @Autowired
    private StepDB db;
    @PostMapping
    public RRO<String> addStep(@RequestBody DTO.AddStepDTO dto)  {
        StepValidator sv = new StepValidator();
        sv.setLab(labDB.findById(dto.getLabId()));
        sv.setTargetObject(equipmentDB.findById(dto.getTargetEquipmentId()));
        sv.setStepNum(dto.getStepNum());
        sv.setTargetTemperature(dto.getTargetTemperature());
        sv.setTargetVolume(dto.getTargetVolume());
        sv.setTargetWeight(dto.getTargetWeight());
        try{
            sv.validate();
        }catch (InvalidFieldException e) {
            return RRO.sendErrorMessage(e.getMessage());
        }
        return super.addEntity(sv);
    }
}
