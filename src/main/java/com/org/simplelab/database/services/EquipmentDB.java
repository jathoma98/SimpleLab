package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.repositories.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class EquipmentDB extends DBService<Equipment> {

    @Autowired
    private EquipmentRepository repository;

    @Override
    public boolean insert(Equipment toInsert) {
        repository.save(toInsert);
        return true;
    }

    @Override
    public boolean deleteById(long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean update(Equipment toUpdate) {
        repository.save(toUpdate);
        return true;
    }

    @Override
    public Equipment findById(long id) {
        return null;
    }
}
