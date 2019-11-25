package com.org.simplelab.database;

import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Wrapper class for handling retrieval and saving of labs
 */
@Transactional
@Component
public class LabDB extends DBService<Lab> {

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

    public boolean updateLab(Lab lab){
        return insert(lab);
    }

    public List<Lab> getLabsByCreatorId(long id){
        return labRepository.findByCreator_id(id);
    }

    @Override
    public Lab findById(long id){
        Optional<Lab> found = labRepository.findById(id);
        return found.isPresent()? found.get() : null;
    }

}
