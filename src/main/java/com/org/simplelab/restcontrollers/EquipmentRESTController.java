package com.org.simplelab.restcontrollers;


import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping(EquipmentRESTController.BASE_MAPPING)
/**
 * Refer to BaseRESTController.java for documentation of basic methods.
 * @Author Jacob Thomas
 */
public class EquipmentRESTController extends BaseRESTController<Equipment> {

    public static final String BASE_MAPPING = "/equipment/rest";

    public static final String EQUIPMENT_ID_MAPPING = "/{equipment_id}";

    @PostMapping
    public RRO<String> addEquipment(@RequestBody EquipmentValidator validator, HttpSession session){
        return super.addEntity(validator, equipmentDB);
    }

    @DeleteMapping(EQUIPMENT_ID_MAPPING)
    public RRO<String> deleteEquipment(@PathVariable("equipment_id") long equipment_id){
        return super.deleteEntity(equipment_id, equipmentDB);
    }

    @PostMapping(EQUIPMENT_ID_MAPPING)
    public RRO<String> updateEquipment(@PathVariable("equipment_id") long equipment_id,
                                       @RequestBody EquipmentValidator dto){
        return super.updateEntity(equipment_id, dto, equipmentDB);
    }

    @GetMapping(EQUIPMENT_ID_MAPPING)
    public Equipment getSpecificEquipment(@PathVariable("equipment_id") long equipment_id){
        return super.getEntityById(equipment_id, equipmentDB);
    }

}
