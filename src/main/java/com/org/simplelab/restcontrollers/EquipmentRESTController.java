package com.org.simplelab.restcontrollers;


import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.services.EquipmentDB;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(EquipmentRESTController.BASE_MAPPING)
@Getter
/**
 * Refer to BaseRESTController.java for documentation of basic methods.
 * @Author Jacob Thomas
 */
public class EquipmentRESTController extends BaseRESTController<Equipment> {

    @Autowired
    private EquipmentDB db;

    public static final String BASE_MAPPING = "/equipment/rest";

    public static final String EQUIPMENT_ID_MAPPING = "/{equipment_id}";

    @PostMapping
    public RRO<String> addEquipment(@RequestBody EquipmentValidator validator, HttpSession session){
        return super.addEntity(validator);
    }

    @DeleteMapping(EQUIPMENT_ID_MAPPING)
    public RRO<String> deleteEquipment(@PathVariable("equipment_id") long equipment_id){
        return super.deleteEntity(equipment_id);
    }

    @PostMapping(EQUIPMENT_ID_MAPPING)
    public RRO<String> updateEquipment(@PathVariable("equipment_id") long equipment_id,
                                       @RequestBody EquipmentValidator dto){
        return super.updateEntity(equipment_id, dto);
    }

    @GetMapping(EQUIPMENT_ID_MAPPING)
    public Equipment getSpecificEquipment(@PathVariable("equipment_id") long equipment_id){
        return super.getEntityById(equipment_id);
    }

}
