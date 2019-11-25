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
public class LabRESTController extends BaseRESTController<Lab> {

    //lab_id = id of the lab to interact with in the DB
    public static final String BASE_MAPPING = "/lab/rest";
    public static final String LAB_ID_MAPPING = "/{lab_id}";

    @Autowired
    LabRepository labRepository;

    /**
     * Attempts to insert a lab into the Lab DB.
     * @param validator - JSON object with format:
     *                  {
     *                      name: the name of the lab
     *                  }
     * @return success:true on successful insert
     *         success:false with error if fields are invalid according to LabValidator.
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> saveLab(@RequestBody LabValidator validator, HttpSession session){
        return super.addEntity(validator, labDB);
    }

    /**
     * Returns a full lab object corresponding to a given lab ID.
     * @param lab_id - id of the lab to be found in the URL
     * @return Lab with Lab.getId() == lab_id, null otherwise
     */
    @GetMapping(LAB_ID_MAPPING)
    public Lab labGet(@PathVariable("lab_id") long lab_id){
        return super.getEntityById(lab_id, labDB);
    }

    /**
     * Deletes lab from the DB with lab_id in the URL
     * @param lab_id - id of the Lab to be deleted in the URL
     * @return success:true
     */
    @DeleteMapping(LAB_ID_MAPPING)
    public Map<String, String> labDelete(@PathVariable("lab_id") long lab_id){
        return super.deleteEntity(lab_id, labDB);
    }

    /**
     * Updates lab in the DB with id lab_id according to sent object
     * @param lab_id - id of the Lab to be updated in the URL
     * @param labUpdateDTO - JSON object with update info
     * @return success:true if lab found and updated
     *         success:false otherwise
     */
    //TODO: add security?
    @PostMapping(LAB_ID_MAPPING)
    public Map<String, String> labUpdate(@PathVariable("lab_id") long lab_id,
                                         @RequestBody LabValidator labUpdateDTO){
        return super.updateEntity(lab_id, labUpdateDTO, labDB);
    }

}
