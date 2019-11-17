package com.org.simplelab.restcontrollers.dto;

import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.validators.CourseValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Master class that contains DTO (Data Transfer Object) classes
 * which handle processing data send through REST endpoints.
 */
public class DTO {

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

    /**
     * Contains information needed to search for Users through the
     * User Search endpoint.
     */
    @Getter
    @Setter
    public class UserSearchDTO {
        private String regex;
    }
}
