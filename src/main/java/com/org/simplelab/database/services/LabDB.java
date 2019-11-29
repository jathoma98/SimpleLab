package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.Step;
import com.org.simplelab.database.repositories.LabRepository;
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
public class LabDB extends DBService<Lab> {

    @Autowired
    private LabRepository repository;

    private class StepSetManager extends EntitySetManager<Step, Lab>{

        public StepSetManager(Collection<Step> set, Lab fullEntity) {
            super(set, fullEntity);
        }

        @Override
        public void checkLegalInsertion(Step toInsert){
            //TODO: make sure steps are in order when inserted
        }

    }


    public boolean insert(Lab toInsert) throws EntityDBModificationException {
        return super.insert(toInsert);
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

    public boolean update(Lab toUpdate) throws DBService.EntityDBModificationException {
        return super.update(toUpdate);
    }

    public Lab findById(long id) {
        return super.findById(id);
    }


    public List<Lab> getLabsByCreatorId(long id){
        return repository.findByCreator_id(id);
    }

    public EntitySetManager<Equipment, Lab> getEquipmentOfLabById(long id){
        Lab found = findById(id);
        if (found == null)
            return null;
        return new EntitySetManager<>(found.getEquipments(), found);
    }
}
