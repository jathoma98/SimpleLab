package com.org.simplelab.database.services.restservice;

import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.repositories.mongodb.LabInstanceRepository;
import com.org.simplelab.database.services.MongoDBService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class LabInstanceDB extends MongoDBService<LabInstance> {

    @Autowired
    private LabInstanceRepository repository;

    @Override
    protected LabInstance getNonexistent() {
        return LabInstance.NO_INSTANCE;
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
