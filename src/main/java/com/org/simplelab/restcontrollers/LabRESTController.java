package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.LabDB;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.validators.LabValidator;
import com.org.simplelab.database.validators.Validator;
import com.org.simplelab.restcontrollers.dto.DTO;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Security;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/lab/rest")
public class LabRESTController {

    //lab_id = id of the lab to interact with in the DB
    public static final String LAB_ID_MAPPING = "/{lab_id}";
    public static final String COURSE_ID_MAPPING = "/{course_id}";

    @Autowired
    LabRepository labRepository;

    @Autowired
    LabDB labDB;

    @Autowired
    CourseDB courseDB;

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
    public Map<String, String> saveLab(@RequestBody LabValidator validator){
        RequestResponse rsp = new RequestResponse();
        try{
            validator.validate();
        } catch (Validator.InvalidFieldException e){
            rsp.setSuccess(false);
            rsp.setError(e.getMessage());
            rsp.set("value", validator.getName());
            return rsp.map();
        }
        Lab lab = validator.build();
        if (labDB.insertLab(lab)){
            rsp.setSuccess(true);
            return rsp.map();
        } else {
            rsp.setSuccess(false);
            return rsp.map();
        }
    }

    /**
     * Returns a full lab object corresponding to a given lab ID.
     * @param lab_id - id of the lab to be found in the URL
     * @return Lab with Lab.getId() == lab_id, null otherwise
     */
    @GetMapping(LAB_ID_MAPPING)
    public Lab labGet(@PathVariable("lab_id") String lab_id){
        Optional<Lab> found = labRepository.findById(lab_id);
        if (found.isPresent())
            return found.get();
        return null;
    }

    /**
     * Deletes lab from the DB with lab_id in the URL
     * @param lab_id - id of the Lab to be deleted in the URL
     * @return success:true
     */
    @DeleteMapping(LAB_ID_MAPPING)
    public Map<String, String> labDelete(@PathVariable("lab_id") String lab_id){
        RequestResponse rsp = new RequestResponse();

        labDB.deleteLabById(lab_id);

        rsp.setSuccess(true);
        return rsp.map();
    }

    /**
     * Updates lab in the DB with id lab_id according to sent object
     * @param lab_id - id of the Lab to be updated in the URL
     * @param labUpdateDTO - JSON object with update info
     * @return success:true if lab found and updated
     *         success:false otherwise
     */
    @PatchMapping(LAB_ID_MAPPING)
    public Map<String, String> labUpdate(@PathVariable("lab_id") String lab_id,
                                         @RequestBody DTO.LabUpdateDTO labUpdateDTO){
        RequestResponse rsp = new RequestResponse();
        System.out.println(lab_id);


        rsp.setSuccess(true);
        return rsp.map();
    }

    /**
     * Right now just returns all labs.
     */
    @GetMapping("")
    public List<Lab> labGetForUser(HttpSession session){
        return labRepository.findAll();
    }

}
