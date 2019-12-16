package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.database.repositories.sql.LabRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.database.services.projections.Projection;
import com.org.simplelab.database.services.projections.Projections;
import com.org.simplelab.database.services.restservice.LabDB;
import com.org.simplelab.database.services.restservice.StepDB;
import com.org.simplelab.database.validators.LabValidator;
import com.org.simplelab.exception.EntityDBModificationException;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.org.simplelab.restcontrollers.LabRESTController.BASE_MAPPING;

@RestController
@RequestMapping(BASE_MAPPING)
@Getter
/**
 * Refer to BaseRESTController.java for documentation of basic methods.
 * @Author Jacob Thomas
 */
public class LabRESTController extends BaseRESTController<Lab> {

    @Autowired
    private LabDB db;

    //lab_id = id of the lab to interact with in the DB
    public static final String BASE_MAPPING = "/lab/rest";
    public static final String LAB_ID_MAPPING = "/{lab_id}";
    public static final String LOAD_LIST_LAB_MAPPING = "/loadLabList";
    public static final String DELETE_MAPPING = "/deleteLab";
    public static final String UPDATE_MAPPING = "/updateLab";
    public static final String LAB_ID_STEP_MAPPING = LAB_ID_MAPPING + "/step";
    public static final String LAB_ID_STEP_NUMBER_MAPPING = LAB_ID_MAPPING + "/{step_number}";
    public static final String LAB_ID_STEP_NUMBER_CHANGE_MAPPING = LAB_ID_MAPPING + "/{step_number}/{move}";
    public static final String LAB_ID_STEP_UPDATE_MAPPING = LAB_ID_MAPPING + "/{step_number}/update";
    public static final String SEARCH_LAB_MAPPING = "/searchLab";
    public static final String LAB_ID_EQUIPMENT_MAPPING = LAB_ID_MAPPING + "/equipment";


    @Autowired
    LabRepository labRepository;


    /**
     * @param validator - JSON object with format:
     *                  {
     *                      name: the name of the lab
     *                  }
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RRO<String> saveLab(@RequestBody LabValidator validator){
        return super.addEntity(validator);
    }

    //Todo need change later, it not safe, anyone change delete others lab here, because we haven't check create_id
    @DeleteMapping(DELETE_MAPPING)
    public RRO<String> deleteLab(@RequestBody DTO.UserLabsDTO toDelete) {
        RRO<String> rro = new RRO();
        for (long lid : toDelete.getLids()) {
            labDB.deleteById(lid);
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    @GetMapping(LAB_ID_MAPPING)
    public RRO<Lab> labGet(@PathVariable("lab_id") long lab_id){
        return super.getEntityById(lab_id);
    }

    @GetMapping(LAB_ID_EQUIPMENT_MAPPING)
    public RRO<List<Equipment>> labGetEquipment(@PathVariable("lab_id") long lab_id){
        List<Equipment> equipments;
        RRO<List<Equipment>> rro = new RRO();
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        try {
            equipments = labDB.getEquipmentOfLabById(lab_id).getAsList();
            rro.setData(equipments);
        } catch (Exception e) {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        }
        return rro;
    }


    @Transactional
    @DeleteMapping(LAB_ID_MAPPING)
    public RRO<String> labDelete(@PathVariable("lab_id") long lab_id){
        return super.deleteEntity(lab_id);
    }

    /**
     * @param labUpdateDTO - JSON object with update info
     */
    //TODO: add security?
    @PostMapping(LAB_ID_MAPPING)
    public RRO<String> labUpdate(@PathVariable("lab_id") long lab_id,
                                 @RequestBody LabValidator labUpdateDTO){
        return super.updateEntity(lab_id, labUpdateDTO);
    }



    @GetMapping(LOAD_LIST_LAB_MAPPING)
    public RRO getListOfLab() {
        long userId = getUserIdFromSession();
        RRO rro = new RRO<Projection>();
        //Use TeacherLabInfo projection to only get attributes we want
        List<Projections.TeacherLabInfo> labs = labDB.getLabsByCreatorId(userId, Projections.TeacherLabInfo.class);
        if (labs == null) {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setData(labs);
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    /**
     * Right now just returns all labs.
     */
    @GetMapping("")
    public Iterable<Lab> labGetForUser() {
        return labRepository.findAll();
    }

    @Transactional
    @PostMapping(LAB_ID_EQUIPMENT_MAPPING)
    public RRO<String> addEquipmentToLab(@PathVariable("lab_id") long lab_id,
                                 @RequestBody long[] ids){
        List<Equipment> toAdd = new ArrayList<>();
        for (long id: ids){
            Equipment found = equipmentDB.findById(id);
            if (found != null)
                toAdd.add(found);
        }
        SQLService.EntitySetManager<Equipment, Lab> toUpdate;
        try {
            toUpdate = labDB.getEquipmentOfLabById(lab_id);
        } catch (Exception e){
            RRO<String> rro = new RRO();
            rro.setMsg("form LAB_ID_EQUIPMENT_MAPPING");
            return rro;
        }
        return super.addEntitiesToEntityList(toUpdate, toAdd);
    }

    @Transactional
    @DeleteMapping(LAB_ID_EQUIPMENT_MAPPING)
    public RRO<String> delEquipmentFromLab(@PathVariable("lab_id") long lab_id,
                                         @RequestBody long[] ids){
        List<Equipment> toDel = new ArrayList<>();
        for (long id: ids){
            Equipment found = equipmentDB.findById(id);
            if (found != null)
                toDel.add(found);
        }
        SQLService.EntitySetManager<Equipment, Lab> toUpdate;
        try {
            toUpdate = labDB.getEquipmentOfLabById(lab_id);
        } catch (Exception e){
            RRO<String> rro = new RRO();
            rro.setMsg("form LAB_ID_EQUIPMENT_MAPPING");
            return rro;
        }
        return super.removeEntitiesFromEntityList(toUpdate, toDel);
    }



    @PatchMapping(UPDATE_MAPPING)
    public RRO<String> updateLab(@RequestBody DTO.LabUpdateDTO dto) {
        long uid = getUserIdFromSession();
        Lab toUpdate = labDB.findById(dto.getLab_id_old());
        if (toUpdate == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }
        if (toUpdate.getCreator().getId() != uid){
            return RRO.sendErrorMessage("Duplicate ID");
        }
        return super.updateEntity(toUpdate.getId(), dto.getNewLabInfo());
    }

//    @GetMapping(LAB_ID_STEP_UPDATE_MAPPING)
//    public RRO<Step> stepUpdate(@PathVariable("lab_id") long lab_id,
//                                     @PathVariable("step_number") int step_num){
//        Lab found = labDB.findById(lab_id);
//        if (found == null){
//            return RRO.sendErrorMessage("Lab Not Found");
//        }
//        List<Step> steps = found.getSteps();
//        Step step=steps.get(step_num-1);
//        RRO<String> rro = new RRO();
//        rro.setData(step);
//        rro.setSuccess(true);
//        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
//        return rro;
//    }

    @GetMapping(LAB_ID_STEP_NUMBER_CHANGE_MAPPING)
    public RRO<String> stepChangeNum(@PathVariable("lab_id") long lab_id,
                                    @PathVariable("step_number") int step_num,
                                    @PathVariable("move") int move){
        Lab found = labDB.findById(lab_id);
        if (found == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }

        List<Step> steps = found.getSteps();
        if((step_num + move) >= steps.size()+1){
            return RRO.sendErrorMessage("This is already last step!");
        }else if ((step_num + move) < 1) {
            return RRO.sendErrorMessage("This is already first step!");
        }

        int total_steps = steps.size();
        for(Step s : steps){
            if(s.getStepNum()==step_num){
                s.setStepNum(step_num + move);
            }else if(s.getStepNum() == step_num + move){
                s.setStepNum(step_num);
            }
        }
        labRepository.save(found);
        RRO<String> rro = new RRO();
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    @Transactional
    @GetMapping(LAB_ID_STEP_MAPPING)
    public RRO getLabStep(@PathVariable("lab_id") long lab_id){
        RRO rro = new RRO();
        Lab found = labDB.findById(lab_id);
        if (found == null){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setMsg("there must be have list of steps in return of rro");
        rro.setData(found.loadAllSteps());
        return rro;
    }

    @Autowired
    StepDB stepDB;

    @PostMapping(LAB_ID_STEP_MAPPING)
    public RRO addStepToLab(@PathVariable("lab_id") long lab_id,
                                    @RequestBody DTO.AddStepDTO dto){
        Lab found = labDB.findById(lab_id);
        if (found == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }
        //TODO: could make better, but for now
        DTO.LabAddStepDTO f_step = new DTO.LabAddStepDTO();
        Equipment targetObject = equipmentDB.findById(dto.getTargetEquipmentId());
        f_step.setStepNum(dto.getStepNum());
        f_step.setTargetObject(targetObject);
        f_step.setTargetName(dto.getTargetName());
        f_step.setTargetTips(dto.getTargetTips());
        f_step.setTargetTemperature(dto.getTargetTemperature());
        f_step.setTargetVolume(dto.getTargetVolume());
        f_step.setTargetWeight(dto.getTargetWeight());
        List<Step> toAdd = new ArrayList<>();
        if(dto.getStepNum() == -1){

            f_step.setStepNum(found.getSteps().size()+1);
            Step s = DBUtils.getMapper().map(f_step, Step.class);
            s.setLab(found);
            try {
                stepDB.insert(s);
            } catch (EntityDBModificationException e){
                RRO.sendErrorMessage(e.getMessage());
            }
            toAdd.add(s);
            return super.addEntitiesToEntityList(labDB.getStepManager(found), toAdd);

        }else{
            Step s = DBUtils.getMapper().map(f_step, Step.class);
            List<Step> steps=found.getSteps();
            int stepNum=f_step.getStepNum();
            for (Step step : steps){
                if(step.getStepNum() == stepNum){
                    step.setTargetName(s.getTargetName());
                    step.setTargetTips(s.getTargetTips());
                    step.setTargetTemperature(s.getTargetTemperature());
                    step.setTargetVolume(s.getTargetVolume());
                    step.setTargetWeight(s.getTargetWeight());
                    s = step;
                    break;
                }
            }

            try{
                if (!s.isNew())
                    stepDB.update(s);
                else
                    stepDB.insert(s);
            }catch (EntityDBModificationException e) {
                e.printStackTrace();
            }
        }
        RRO rro = new RRO();
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    @DeleteMapping(LAB_ID_STEP_NUMBER_MAPPING)
    public RRO DeleteStepFromLab(@PathVariable("lab_id") long lab_id, @PathVariable("step_number") int stepNun){
            RRO<List<Step>> rro = new RRO();
        Lab found = labDB.findById(lab_id);
        if (found == null){
            return RRO.sendErrorMessage("Lab Not Found");
        }
        List<Step> steps = found.getSteps();
        for(int i = 0; i < steps.size(); i++){
            Step step = steps.get(i);
            if(step.getStepNum() > stepNun){
                step.setStepNum(step.getStepNum()-1);
            }else if(step.getStepNum() == stepNun){
                steps.remove(step);
                --i;
            }
        }
        found.setSteps(steps);
        try {
            labDB.update(found);
        } catch (EntityDBModificationException e){
            RRO.sendErrorMessage(e.getMessage());
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }


    @Transactional
    @DeleteMapping(LAB_ID_STEP_MAPPING)
    public RRO deleteAllStepsFromLab(@PathVariable("lab_id") long lab_id){
        Lab found = labDB.findById(lab_id);
        List<Step> steps = found.getSteps();
        for (Step s: steps)
            s.setLab(null);
        steps.clear();
        try {
            labDB.update(found);
        } catch (EntityDBModificationException e){
            RRO.sendErrorMessage(e.getMessage());
        }
        Lab found_after_delete = labDB.findById(lab_id);
        RRO<Lab> rro = new RRO<>();
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    @PostMapping(SEARCH_LAB_MAPPING)
    public RRO<List<Lab>> searchCourse(@RequestBody DTO.UserSearchDTO toSearch){
        RRO<List<Lab>> rro = new RRO();
        String courseRegex = toSearch.getRegex();
        //dont allow empty searches
        if (courseRegex == null || courseRegex.equals("")){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(labDB.searchLabWithKeyword(courseRegex));
        return rro;
    }
}
