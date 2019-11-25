package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.validators.LabValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

}
