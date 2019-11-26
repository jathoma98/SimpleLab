package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.repositories.EquipmentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@Getter
public class EquipmentDB extends DBService<Equipment> {

    @Autowired
    private EquipmentRepository repository;

}
