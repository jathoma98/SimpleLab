package com.org.simplelab.database;

import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Wrapper class for handling retrieval and saving of labs
 */
@Transactional
@Component
public class LabDB {

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private CourseRepository courseRepository;

    public void deleteLabById(long id){
        labRepository.deleteById(id);
    }

    public boolean insertLab(Lab lab){
        labRepository.save(lab);
        return true;

    }

}
