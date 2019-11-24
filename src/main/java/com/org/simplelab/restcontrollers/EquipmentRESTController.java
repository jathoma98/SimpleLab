package com.org.simplelab.restcontrollers;


import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.database.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PostLoad;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping(EquipmentRESTController.BASE_MAPPING)
public class EquipmentRESTController {

    public static final String BASE_MAPPING = "/equipment/rest";

    public static final String EQUIPMENT_ID_MAPPING = "/{equipment_id}";

    @Autowired
    UserDB userDB;


    @PostMapping
    public Map addEquipment(@RequestBody EquipmentValidator validator, HttpSession session){
        RequestResponse rsp = new RequestResponse();
        long user_id = (long)session.getAttribute("user_id");
        try{
            validator.validate();
        } catch (Validator.InvalidFieldException e){
            rsp.setError(e.getMessage());
            return rsp.map();
        }
        Equipment e = validator.build();
        User creator = userDB.findUserById(user_id);
        e.setCreator(creator);

        return rsp.map();

    }

}
