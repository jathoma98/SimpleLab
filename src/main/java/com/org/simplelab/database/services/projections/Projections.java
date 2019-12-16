package com.org.simplelab.database.services.projections;

import com.org.simplelab.database.entities.sql.Equipment;
import lombok.Value;

import java.util.Collection;
import java.util.List;

public class Projections {

    /** For showing list of labs for teacher info page. */
    @Value
    public static class TeacherLabInfo implements Projection{
        String name, createdDate;
        String description;
        long id;
    }

    /** For showing list of courses for teacher info page.
     * We can't use course_id in projection, because JPA doesnt like underscores
     * so we have to manually copy fields. */
    @Value
    public static class TeacherCourseInfo implements Projection{
        String name, createdDate, course_id;
    }

    @Value
    public static class TeacherEquipmentInfo implements Projection{
        String name, createdDate, type;
        long id;
    }

    public interface TeacherEditEquipmentInfo extends Projection{
        long getId();
        String getName();
        String getType();
        String getCreatedDate();
        Collection<PropertyView> getProperties();
        ImageFileView getImg();

        interface PropertyView extends Projection{
            String getPropertyKey();
            String getPropertyValue();
        }

        interface ImageFileView extends Projection{
            byte[] getData();
        }

    }

    public interface TeacherGetStepsOfLab extends Projection{
        List<StepView> getSteps();

        interface StepView extends Projection{
            String getTargetVolume();
            String getTargetWeight();
            String getTargetTemperature();
            String getTargetName();
            String getTargetTips();
            Equipment getTargetObject();
        }
    }
}
