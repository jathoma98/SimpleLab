package com.org.simplelab.restcontrollers.dto;

import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.validators.CourseValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains fields needed to update a Course through
 * a REST endpoint.
 */

@Getter
@Setter
@ToString
public class CourseUpdateDTO {

    private String course_id_old;
    private CourseValidator newCourseInfo;

}
