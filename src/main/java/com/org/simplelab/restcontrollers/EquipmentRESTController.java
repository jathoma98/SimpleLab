package com.org.simplelab.restcontrollers;


import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.services.EquipmentDB;
import com.org.simplelab.database.services.projections.Projection;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.restcontrollers.rro.RRO_ACTION_TYPE;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    public static final String EQUIPMENT_LIST_MAPPING = "/loadEquipmentList";

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RRO<String> saveEquipment(@RequestBody EquipmentValidator validator){
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

    @GetMapping(EQUIPMENT_LIST_MAPPING)
    public RRO<List<Projection.TeacherEquipmentInfo>> getListOfEquipments (HttpSession session){
        long userId = getUserIdFromSession();
        RRO rro = new RRO<Projection>();
        //Use TeacherLabInfo projection to only get attributes we want
        List<Projection.TeacherEquipmentInfo> equips = equipmentDB.getEquipmentByCreatorId(userId, Projection.TeacherEquipmentInfo.class);
        if (equips == null) {
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setData(equips);
        rro.setSuccess(true);
        rro.setAction(RRO_ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

}
