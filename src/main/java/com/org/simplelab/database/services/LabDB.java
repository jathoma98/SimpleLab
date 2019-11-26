package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.LabRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Wrapper class for handling retrieval and saving of labs
 */
@Transactional
@Component
@Getter
public class LabDB extends DBService<Lab> {

    @Autowired
    private LabRepository repository;

    public boolean insert(Lab toInsert) throws EntityInsertionException {
        return super.insert(toInsert);
    }

    public boolean deleteById(long id) {
        return super.deleteById(id);
    }

    public boolean update(Lab toUpdate) {
        return super.update(toUpdate);
    }

    public Lab findById(long id) {
        return super.findById(id);
    }


    private class EquipmentSetManager extends EntitySetManager<Equipment, Lab>{
        public EquipmentSetManager(Set<Equipment> set, Lab lab){
            super(set, lab);
        }
    }


    public List<Lab> getLabsByCreatorId(long id){
        return repository.findByCreator_id(id);
    }

    public EquipmentSetManager getEquipmentOfLabById(long id){
        Lab found = findById(id);
        if (found == null)
            return null;
        return new EquipmentSetManager(found.getEquipments(), found);
    }
}
