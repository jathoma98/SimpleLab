package com.org.simplelab.restcontrollers;


import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.database.validators.InvalidFieldException;
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
public class EquipmentRESTController extends BaseRESTController<Equipment> {

    public static final String BASE_MAPPING = "/equipment/rest";

    public static final String EQUIPMENT_ID_MAPPING = "/{equipment_id}";

    @PostMapping
    public Map addEquipment(@RequestBody EquipmentValidator validator, HttpSession session){
        return super.addEntity(validator, session, equipmentDB);
    }

}
