package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lab/rest")
public class LabRESTController {

    //lab_id = id of the lab to interact with in the DB
    public static final String LAB_ID_MAPPING = "/{lab_id}";

    @Autowired
    LabRepository labRepository;

    /**
     * Returns a full lab object corresponding to a given lab ID.
     * @param lab_id - id of the lab to be found in the URL
     * @return Lab with Lab.get_id() == lab_id
     */
    @GetMapping(LAB_ID_MAPPING)
    public Lab labGet(@PathVariable("lab_id") String lab_id){
        System.out.println(lab_id);
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
        System.out.println(lab_id);

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
