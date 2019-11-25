package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
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
public class LabDB extends DBService<Lab> {

    private class EquipmentSetManager extends EntitySetManager<Equipment, Lab>{
        public EquipmentSetManager(Set<Equipment> set, Lab lab){
            super(set, lab);
        }
    }

    @Override
    public boolean deleteById(long id){
        labRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean insert(Lab lab){
        labRepository.save(lab);
        return true;
    }

    @Override
    public boolean update(Lab lab){
        return insert(lab);
    }

    public List<Lab> getLabsByCreatorId(long id){
        return labRepository.findByCreator_id(id);
    }


    public EquipmentSetManager getEquipmentOfLabById(long id){
        Lab found = findById(id);
        if (found == null)
            return null;
        return new EquipmentSetManager(found.getEquipments(), found);
    }

    @Override
    public Lab findById(long id){
        Optional<Lab> found = labRepository.findById(id);
        return found.isPresent()? found.get() : null;
    }


}
