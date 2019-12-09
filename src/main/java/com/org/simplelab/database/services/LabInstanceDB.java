package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.repositories.mongodb.LabInstanceRepository;
import com.org.simplelab.database.repositories.sql.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabInstanceDB {

    @Autowired
    private LabInstanceRepository repository;

    public boolean insert(LabInstance toInsert){
        repository.save(toInsert);
        return true;
    }

    public boolean update(LabInstance toUpdate){
        repository.save(toUpdate);
        return true;
    }

    public LabInstance findInstanceByLabId(long lab_id){
        //todo: this needs to check user id too
        List<LabInstance> found = repository.findByLabId(lab_id);
        if (found == null || found.size() == 0){
            return LabInstance.NO_INSTANCE;
        }
        return found.get(0);

    }

}
