package com.org.simplelab.restcontrollers;


import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.EquipmentProperty;
import com.org.simplelab.database.services.projections.Projection;
import com.org.simplelab.database.services.projections.Projections;
import com.org.simplelab.database.services.restservice.EquipmentDB;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
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
    public static final String UPDATE_MAPPING = "/updateEquipment";

    public static final String DELETE_MAPPING = "/deleteEquipment";
    public static final String EQUIPMENT_LIST_MAPPING = "/loadEquipmentList";
    public static final String EQUIPMENT_OBJ_LIST_MAPPING = "/loadEquipmentObjList";
    public static final String ADD_IMG_MAPPING = EQUIPMENT_ID_MAPPING + "/img";


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

    @Transactional
    @GetMapping(EQUIPMENT_ID_MAPPING)
    public RRO<Equipment> getSpecificEquipment(@PathVariable("equipment_id") long equipment_id){
        RRO<Equipment> rro = super.getEntityById(equipment_id);
        return rro;
    }

    @GetMapping(EQUIPMENT_LIST_MAPPING)
    public RRO<List<Projections.TeacherEquipmentInfo>> getListOfEquipments (HttpSession session){
        long userId = getUserIdFromSession();
        RRO rro = new RRO<Projection>();
        //Use TeacherLabInfo projection to only get attributes we want
        List<Projections.TeacherEquipmentInfo> equips = equipmentDB.getEquipmentByCreatorId(userId, Projections.TeacherEquipmentInfo.class);
        if (equips == null) {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setData(equips);
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    @GetMapping(EQUIPMENT_OBJ_LIST_MAPPING)
    public RRO<List<Equipment>> getListOfEquipmentsObj (HttpSession session){
        long userId = getUserIdFromSession();
        RRO rro = new RRO<Projection>();
        List<Equipment> equips = equipmentDB.getEquipmentByCreatorId(userId);
        if (equips == null) {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setData(equips);
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    @DeleteMapping(DELETE_MAPPING)
    public RRO<String> deleteEquipment(@RequestBody Long[] toDelete,
                                    HttpSession session) {
        RRO<String> rro = new RRO();
        long userId =  getUserIdFromSession();
        for (Long id : toDelete) {
            //TODO: check ownership
            System.out.println(equipmentDB.checkUsingEquipment(id));
            if (!equipmentDB.checkUsingEquipment(id).isEmpty()){
                rro.setAction(RRO.ACTION_TYPE.PRINT_MSG.name());
                rro.setMsg("ID: "+id+"  "+ RRO.MSG.EQUIPMENT_DELETE_ERROR.getMsg());
                return rro;
            }
            equipmentDB.deleteById(id);
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    @PatchMapping(UPDATE_MAPPING)
    public RRO<String> updateEquipment(@RequestBody DTO.EquipmentUpdateDTO dto) {
        long uid = getUserIdFromSession();
        Equipment toUpdate = equipmentDB.findById(dto.getEquipment_id_old());
        if (toUpdate == null){
            return RRO.sendErrorMessage("Equipment Not Found");
        }
        if (toUpdate.getCreator().getId() != uid){
            return RRO.sendErrorMessage("Duplicate ID");
        }
        //TODO: make this more efficient
        dto.getNewEquipmentInfo().getProperties().forEach((p)->{
            for(EquipmentProperty ep : toUpdate.getProperties()){
                if(ep.getPropertyKey().equals(p.getPropertyKey())){
                    ep.setPropertyValue(p.getPropertyValue());
                    break;
                }
            }
        });
        EquipmentValidator newEV = new EquipmentValidator();
        newEV.setName(dto.getNewEquipmentInfo().getName());
        newEV.setType(dto.getNewEquipmentInfo().getType());
        return super.updateEntity(toUpdate.getId(), newEV);
    }
}
