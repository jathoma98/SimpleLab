package com.org.simplelab.database.services.projections;

import lombok.Value;

import java.util.Collection;

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
}
