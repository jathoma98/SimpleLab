package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.LabValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.restcontrollers.rro.RRO_ACTION_TYPE;
import com.org.simplelab.restcontrollers.rro.RRO_MSG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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
    public static final String COURSE_ID_MAPPING = "/{course_id}";
    public static final String LOAD_LIST_LAB_MAPPING = "/loadLabList";
    public static final String UPDATE_MAPPING = "/updateLab";
    public static final String DELETE_MAPPING = "/deleteLab";


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
    public Iterable<Lab> labGetForUser(HttpSession session) {
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
        RRO<String> rro = new RRO();
        long uid = getUserIdFromSession(session);
        Lab toUpdate = labDB.findById(dto.getLab_id_old());
        if (toUpdate == null){
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg("Lab Not Found");
            rro.setSuccess(false);
            return rro;
        }
        if (toUpdate.getCreator().getId() != uid){
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg("Duplicate ID");
            rro.setSuccess(false);
            return rro;
        }
        return super.updateEntity(toUpdate.getId(), dto.getNewLabInfo(), labDB);
    }
}
