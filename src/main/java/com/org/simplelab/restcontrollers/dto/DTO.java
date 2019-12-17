package com.org.simplelab.restcontrollers.dto;

import com.org.simplelab.database.entities.mongodb.InstantiatedEquipment;
import com.org.simplelab.database.entities.mongodb.StepRecord;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.database.validators.LabValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * Describes objects responsible for transfer from the view layer
 * to backend APIs
 */
public abstract class DTO {

    /**
     * Contains fields needed to update a Course through
     * a REST endpoint.
     */
    @Getter
    @Setter
    public static class CourseUpdateDTO extends DTO {

        private String course_id_old;
        private CourseValidator newCourseInfo;

    }

    /**
     * Contains information needed to search for Users through the
     * User Search endpoint.
     */
    @Getter
    @Setter
    public static class UserSearchDTO extends DTO {
        private String regex;
    }

    @Getter
    @Setter
    public static class CourseSearchDTO extends DTO {
        private String regex;
    }

    /**
     * Contains information for updating a lab.
     */
    @Getter
    @Setter
    public static class LabUpdateDTO extends DTO {
        private Long lab_id_old;
        private LabValidator newLabInfo;
    }

    @Getter
    @Setter
    public static class EquipmentUpdateDTO extends DTO {
        private Long equipment_id_old;
        private EquipmentValidator newEquipmentInfo;
    }

    /**
     * Contains information for updating student list in a course.
     */
    @Getter
    @Setter
    public static class CourseUpdateStudentListDTO extends DTO {
        //course need to be update
        private String course_id;
        private String invite_code;
        //list of username need to add or delete
        private List<String> usernameList;
    }

    @Getter
    @Setter
    public static class CourseUpdateLabListDTO extends DTO {

        //list of username need to add or delete
        private long[] lab_ids;
        //course need to be update
        private String course_id;
    }

    @Getter
    @Setter
    public static class LoadLabListDTO extends DTO {
        //course need to be update
        private String course_id;
    }

    /**
     * Contains info to add a lab to a course.
     */
    @Getter
    @Setter
    public static class CourseAddLabsDTO extends DTO {
        //ids of labs to add
        private long[] lab_ids;
        //course_id of course to add labs to
        private String course_id;
    }

    @Getter
    @Setter
    public static class LabAddEquipmentDTO extends DTO {
        //ids of labs to add
        private long[] equipments;
        //course_id of course to add labs to
        private long lab_id;
    }

    @Getter
    @Setter
    public static class UserLabsDTO extends DTO{
        private long[] uid;
        private String username;
        private long[] lids;
    }


    @Getter
    @Setter
    public static class fpUserInput extends DTO{
        private String answer;
        private User user;
    }

     @Getter
     @Setter
     public static class LabAddStepDTO extends DTO{
         private Equipment targetObject;
         private int stepNum;
         private String targetTemperature;
         private String targetName;
         private String targetTips;
         private String targetVolume;
         private String targetWeight;
     }

     @Getter
     @Setter
     public static class EquipmentInteractionDTO extends DTO{
        private Equipment object1, object2;
        private String parameter;
        private int stepNum;

        public EquipmentInteractionDTO(){
            parameter = "0";
        }
     }

     @Getter
     @Setter
     public static class AddRecipeDTO extends DTO{
         Long equipmentOne;
         Long equipmentTwo;
         Long result;
         int ratioOne;
         int ratioTwo;
         int ratioThree;
     }

    @Getter
    @Setter
    public static class AddStepDTO extends DTO{
        Long labId;
        Long targetEquipmentId;
        int stepNum = 0;
        String targetTemperature;
        String targetName;
        String targetTips;
        String targetVolume;
        String targetWeight;
    }

    @Getter
    @Setter
    public static class LabSaveStateDTO extends DTO{
        private Collection<InstantiatedEquipment> equipments;
        private List<StepRecord> step;
        private boolean labFinished = false;
    }
}

