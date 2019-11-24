package com.org.simplelab.restcontrollers.dto;

import com.org.simplelab.database.validators.CourseValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    public static class CourseUpdateDTO {

        private String course_id_old;
        private CourseValidator newCourseInfo;

    }

    /**
     * Contains information needed to search for Users through the
     * User Search endpoint.
     */
    @Getter
    @Setter
    public static class UserSearchDTO {
        private String regex;
    }

    /**
     * Contains information for updating a lab.
     */
    @Getter
    @Setter
    public static class LabUpdateDTO {
        private String temp;
    }

    /**
     * Contains information for updating student list in a course.
     */
    @Getter
    @Setter
    public static class CourseUpdateStudentListDTO {
        //course need to be update
        private String course_id;
        //list of username need to add or delete
        private List<String> usernameList;
    }
}
