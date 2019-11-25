package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.validators.LabValidator;
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
    public Map<String, String> saveLab(@RequestBody LabValidator validator, HttpSession session){
        return super.addEntity(validator, labDB);
    }

    @GetMapping(LAB_ID_MAPPING)
    public Lab labGet(@PathVariable("lab_id") long lab_id){
        return super.getEntityById(lab_id, labDB);
    }

    @DeleteMapping(LAB_ID_MAPPING)
    public Map<String, String> labDelete(@PathVariable("lab_id") long lab_id){
        return super.deleteEntity(lab_id, labDB);
    }

    /**
     * @param labUpdateDTO - JSON object with update info
     */
    //TODO: add security?
    @PostMapping(LAB_ID_MAPPING)
    public Map<String, String> labUpdate(@PathVariable("lab_id") long lab_id,
                                         @RequestBody LabValidator labUpdateDTO){
        return super.updateEntity(lab_id, labUpdateDTO, labDB);
    }



    @GetMapping(LOAD_LIST_LAB_MAPPING)
    public List<Lab> getListOfCourse(HttpSession session) {
        long userId = -1;
        try {
            userId = (long) session.getAttribute("user_id");
        } catch (Exception e) {
            //redirect to login
        }
        List<Lab> labs = labDB.getLabsByCreatorId(userId);
        return labs;
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
    public Map addEquipmentToLab(@PathVariable("lab_id") long lab_id,
                                 @RequestBody List<Equipment> equipmentToAdd){
        RequestResponse response = new RequestResponse();
        DBService.EntitySetManager<Equipment, Lab> found = labDB.getEquipmentOfLabById(lab_id);
        if (found == null){
            response.setError("Lab not found.");
            return response.map();
        }
        return super.addEntitiesToEntityList(found, equipmentToAdd, labDB);
    }

}
