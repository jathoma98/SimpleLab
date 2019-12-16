package com.org.simplelab.database.services.restservice;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.database.repositories.sql.LabRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.exception.EntityDBModificationException;
import com.org.simplelab.exception.EntitySetModificationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Wrapper class for handling retrieval and saving of labs
 */
@Transactional
@Component
@Getter
public class LabDB extends SQLService<Lab> {
    public enum ERRORS{
        E_WRONGSTEPORDER("Invalid ordering of steps. Make sure steps are ordered sequentially from 1,2,..."),
        E_DUPSTEP("Attempted to insert step with duplicate step number.");

        private final String msg;
        ERRORS(String s){
            this.msg = s;
        }
        public String getMsg(){
            return this.msg;
        }
    }

    @Autowired
    private LabRepository repository;

    private class StepSetManager extends EntitySetManager<Step, Lab>{


        public StepSetManager(Collection<Step> set, Lab fullEntity) {
            super(set, fullEntity);
        }

        @Override
        public void checkLegalInsertion(Step toInsert) throws EntitySetModificationException {
            //make sure there is a step with where stepnum = toInsert.stepnum -1
            //to ensure sequential ordering
            long countPredecessor = this.getEntitySet().stream()
                    .filter(step -> step.getStepNum() == toInsert.getStepNum()-1)
                    .count();
            if (toInsert.getStepNum() != 1 && countPredecessor != 1){
                throw new EntitySetModificationException(ERRORS.E_WRONGSTEPORDER.getMsg());
            }

            //then, ensure there isnt a duplicate step with this stepnum
            long countDuplicates = this.getEntitySet().stream()
                    .filter(step -> step.getStepNum() == toInsert.getStepNum())
                    .count();
            if (countDuplicates > 0){
                throw new EntitySetModificationException(ERRORS.E_DUPSTEP.getMsg());
            }
        }

    }



    public boolean deleteById(long id) {
        return super.deleteById(id);
    }

    public EntitySetManager<Step, Lab> getStepsOfLabById(long id){
        Lab found = findById(id);
        if (found == null)
            return null;
        return getStepManager(found);
    }

    public EntitySetManager<Step, Lab> getStepManager(Lab l){
        return new StepSetManager(l.getSteps(), l);
    }

    public boolean update(Lab toUpdate) throws EntityDBModificationException {
        return super.update(toUpdate);
    }

    public Lab findById(long id) {
        return super.findById(id);
    }


    public List<Lab> getLabsByCreatorId(long id){
        return repository.findByCreator_id(id, Lab.class);
    }

    public <T> List<T> getLabsByCreatorId(long id, Class<T> projection){
        return repository.findByCreator_id(id, projection);
    }

    public EntitySetManager<Equipment, Lab> getEquipmentOfLabById(long id){
        Lab found = findById(id);
        if (found == null)
            return null;
        return new EntitySetManager<>(found.getEquipments(), found);
    }

    public List<Lab> searchLabWithKeyword(String keyword) {
        return repository.searchLabWithKeyword(keyword);
    }
//    @Override
//    public Lab findById(long id){
//        Optional<Lab> found = labRepository.findById(id);
//        return found.isPresent()? found.get() : null;
//    }



}
