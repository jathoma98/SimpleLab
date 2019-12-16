package com.org.simplelab.database.services.restservice;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.EquipmentProperty;
import com.org.simplelab.database.repositories.sql.EquipmentRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.exception.EntityDBModificationException;
import com.org.simplelab.exception.EntitySetModificationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional
@Component
@Getter
public class EquipmentDB extends SQLService<Equipment> {

    private class EquipmentPropertySetManager extends
            EntitySetManager<EquipmentProperty, Equipment>{
        EquipmentPropertySetManager(Collection<EquipmentProperty> set, Equipment eq){
            super(set, eq);
        }

        @Override
        public void insert(EquipmentProperty toInsert) throws EntitySetModificationException {
            toInsert.setParentEquipment(this.getFullEntity());
            super.insert(toInsert);
        }
    }

    @Autowired
    private EquipmentRepository repository;

    /**
     * This should be how you insert and delete properties to an Equipment entity. Dont
     * use getProperties() on the object itself.
     * @param e
     * @return
     */
    public EntitySetManager<EquipmentProperty, Equipment> getPropertiesManager(Equipment e){
        return new EquipmentPropertySetManager(e.getProperties(), e);
    }


    public boolean deleteById(long id) {
        return super.deleteById(id);
    }

    public boolean update(Equipment toUpdate) throws EntityDBModificationException {
        return super.update(toUpdate);
    }

    public Equipment findById(long id) {
        return super.findById(id);
    }

    public EntitySetManager<EquipmentProperty, Equipment> getPropertiesOfEquipment(long id){
        Equipment e = findById(id);
        if (e == null)
            return null;
        return new EquipmentPropertySetManager(e.getProperties(), e);
    }

    public <T> List<T> getEquipmentByCreatorId(long id, Class<T> projection){
        return repository.findByCreator_id(id, projection);
    }

    public List<Equipment> getEquipmentByCreatorId(long id){
        return repository.findByCreator_id(id);
    }

    public List<String> checkUsingEquipment(long id){return repository.checkUsingEquipment(id);}



}
