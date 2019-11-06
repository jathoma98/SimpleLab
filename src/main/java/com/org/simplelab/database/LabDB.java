package com.org.simplelab.database;

import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.LabelView;

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

    public boolean insertLab(Lab lab, Course course){

        /**
         * PSEUDOCODE:
         *
         * lab := user created lab, this should have already been validated by a LabValidator class
         * course := course to which the lab belongs, cannot add lab without course
         *
         * lab -> labRepository.save(lab)
         * labs := course.getLabs()
         * lab -> labs.add(lab)
         * course -> courseRepository.save(course)
         *
         */

        return false;
    }

}
