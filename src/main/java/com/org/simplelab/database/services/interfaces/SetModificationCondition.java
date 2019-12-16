package com.org.simplelab.database.services.interfaces;

import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.exception.EntitySetModificationException;

/**
 * Methods which implement checks for insertion and deletion
 * of elements from an EntitySetManager.
 */
public interface SetModificationCondition<T extends BaseTable> {

    /**
     * Validates an attempted insertion on the given EntitySet
     * @throws Exception - If the attempted insertion violates a constraint of the set
     */
     default void checkLegalInsertion(T toInsert)
             throws EntitySetModificationException {
         //no conditions by default
    }

    /**
     * Validates an attempted deletion on the given EntitySet
     * @throws Exception - If the attempted insertion violates a constraint of the set
     */
    default void checkLegalDeletion(T toDelete)
            throws EntitySetModificationException{
         //no conditions by default
    }

}
