package com.org.simplelab.database.services.projections;

/**
 * Defines projections of DB queries.
 *
 * ex:
 * Running LabDB.findLabsByCreatorId(id, TeacherLabInfo.class) will return only the fields
 * listed in the TeacherLabInfo class, not the entire Lab object.
 *
 * All classes need @Value annotation (from lombok, not Spring)
 */
public interface Projection {


}
