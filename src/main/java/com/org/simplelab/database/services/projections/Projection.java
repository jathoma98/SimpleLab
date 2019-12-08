package com.org.simplelab.database.services.projections;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.EquipmentProperty;
import com.org.simplelab.database.entities.Step;
import lombok.Value;

import java.util.List;
import java.util.Set;

/**
 * Defines projections of DB queries.
 *
 * ex:
 * Running LabDB.findLabsByCreatorId(id, TeacherLabInfo.class) will return only the fields
 * listed in the TeacherLabInfo class, not the entire Lab object.
 *
 * All classes need @Value annotation (from lombok, not Spring)
 */
public abstract class Projection {

    /** For showing list of labs for teacher info page. */
    @Value
    public static class TeacherLabInfo extends Projection{
        String name, createdDate;
        long id;
    }

    /** For showing list of courses for teacher info page.
     * We can't use course_id in projection, because JPA doesnt like underscores
     * so we have to manually copy fields. */
    @Value
    public static class TeacherCourseInfo extends Projection{
        String name, createdDate, course_id;
    }

    @Value
    public static class TeacherEquipmentInfo extends Projection{
        String name, createdDate;
        long id;
    }
}
