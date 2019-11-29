package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.Step;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.validators.LabValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.restcontrollers.rro.RRO_ACTION_TYPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.org.simplelab.restcontrollers.LabRESTController.BASE_MAPPING;

@RestController
@RequestMapping(BASE_MAPPING)
/**
 * Refer to BaseRESTController.java for documentation of basic methods.
 * @Author Jacob Thomas
 */
public class LabRESTController extends BaseRESTController<Lab> {

    //lab_id = id of the lab to interact with in the DB
    public static final String BASE_MAPPING = "/lab/rest";
    public static final String LAB_ID_MAPPING = "/{lab_id}";
    public static final String LOAD_LIST_LAB_MAPPING = "/loadLabList";
    public static final String UPDATE_MAPPING = "/updateLab";
    public static final String LAB_ID_STEP_MAPPING = LAB_ID_MAPPING + "/step";
    public static final String LAB_ID_STEP_NUMBER_MAPPING = LAB_ID_MAPPING + "/{step_number}";


    @Autowired
    LabRepository labRepository;
    public static final String LAB_ID_EQUIPMENT_MAPPING = LAB_ID_MAPPING + "/equipment";


    /**
     * @param validator - JSON object with format:
     *                  {
     *                      name: the name of the lab
     *                  }
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RRO<String> saveLab(@RequestBody LabValidator validator){
        return super.addEntity(validator, labDB);
    }

    @GetMapping(LAB_ID_MAPPING)
    public RRO<Lab> labGet(@PathVariable("lab_id") long lab_id){
        RRO<Lab> rro = new RRO();
        rro.setSuccess(true);
        rro.setData(super.getEntityById(lab_id, labDB));
        rro.setAction(RRO_ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    @DeleteMapping(LAB_ID_MAPPING)
    public RRO<String> labDelete(@PathVariable("lab_id") long lab_id){
        return super.deleteEntity(lab_id, labDB);
    }

    /**
     * @param labUpdateDTO - JSON object with update info
     */
    //TODO: add security?
    @PostMapping(LAB_ID_MAPPING)
    public RRO<String> labUpdate(@PathVariable("lab_id") long lab_id,
                                 @RequestBody LabValidator labUpdateDTO){
        return super.updateEntity(lab_id, labUpdateDTO, labDB);
    }



    @GetMapping(LOAD_LIST_LAB_MAPPING)
    public RRO<List<Lab>> getListOfCourse(HttpSession session) {
        long userId = getUserIdFromSession(session);
        RRO rro = new RRO();
        List<Lab> labs = labDB.getLabsByCreatorId(userId);
        if (labs == null) {
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setData(labs);
        rro.setSuccess(true);
        rro.setAction(RRO_ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    /**
     * Right now just returns all labs.
     */
    @GetMapping("")
    public Iterable<Lab> labGetForUser() {
        return labRepository.findAll();
    }

    @Transactional
    @PostMapping(LAB_ID_EQUIPMENT_MAPPING)
    public RRO<String> addEquipmentToLab(@PathVariable("lab_id") long lab_id,
                                 @RequestBody List<Equipment> equipmentToAdd){
        RRO<String> rro = new RRO();
        DBService.EntitySetManager<Equipment, Lab> found = labDB.getEquipmentOfLabById(lab_id);
        if (found == null){
            rro.setMsg("Lab not found.");
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            return rro;
        }
        return super.addEntitiesToEntityList(found, equipmentToAdd, labDB);
    }


    @PatchMapping(UPDATE_MAPPING)
    public RRO<String> updateLab(@RequestBody DTO.LabUpdateDTO dto, HttpSession session) {
        long uid = getUserIdFromSession(session);
        Lab toUpdate = labDB.findById(dto.getLab_id_old());
        if (toUpdate == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }
        if (toUpdate.getCreator().getId() != uid){
            return RRO.sendErrorMessage("Duplicate ID");
        }
        return super.updateEntity(toUpdate.getId(), dto.getNewLabInfo(), labDB);
    }

    @PostMapping(LAB_ID_STEP_MAPPING)
    public RRO<String> addStepToLab(@PathVariable("lab_id") long lab_id,
                                    @RequestBody DTO.LabAddStepDTO dto){
        Lab found = labDB.findById(lab_id);
        if (found == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }
        Step s = DBUtils.getMapper().map(dto, Step.class);
        s.setLab(found);
        List<Step> toAdd = new ArrayList<>();
        toAdd.add(s);
        return super.addEntitiesToEntityList(labDB.getStepManager(found), toAdd, labDB);
    }

    //TODO: implement delete step mapping later, we should get a prototype lab working first
    /**
     * THIS IS A TESTING METHOD - deletes all steps in the lab
     * @return RRO with data field containing the updated lab
     */
    @DeleteMapping(LAB_ID_STEP_MAPPING)
    public RRO<Lab> deleteAllStepsFromLab(@PathVariable("lab_id") long lab_id){
        Lab found = labDB.findById(lab_id);
        found.setSteps(new ArrayList<>());
        try {
            labDB.update(found);
        } catch (DBService.EntityDBModificationException e){
            RRO.sendErrorMessage(e.getMessage());
        }
        RRO<Lab> rro = new RRO<>();
        rro.setSuccess(true);
        rro.setAction(RRO_ACTION_TYPE.LOAD_DATA.name());
        rro.setData(found);
        return rro;
    }
}
