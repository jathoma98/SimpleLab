package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.repositories.mongodb.LabInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LabInstanceDB {
    //TODO: migrate this to use DBService

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

    public LabInstance findById(String id){
        Optional<LabInstance> found = repository.findById(id);
        return found.isPresent() ? found.get() : LabInstance.NO_INSTANCE;
    }

    public List<LabInstance> findByLabIdAndUserId(long lab_id, long user_id){
        List<LabInstance> found = repository.findByLabIdAndUserId(lab_id, user_id);
        return found;
    }

    public LabInstance findActiveLab(long labId, long userId){
        List<LabInstance> found = repository.findActiveInstanceOfLab(labId, userId);
        if (found.size() > 0)
            return found.get(0);
        return LabInstance.NO_INSTANCE;
    }

}
