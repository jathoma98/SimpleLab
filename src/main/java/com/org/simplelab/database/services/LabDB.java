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


    private class EquipmentSetManager extends EntitySetManager<Equipment, Lab>{
        public EquipmentSetManager(Set<Equipment> set, Lab lab){
            super(set, lab);
        }
    }

    @Override
    public boolean deleteById(long id){
        Lab found = findById(id);
        if (found != null) {
            found.setEquipments(null);
            repository.delete(found);
        }
        return true;
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
