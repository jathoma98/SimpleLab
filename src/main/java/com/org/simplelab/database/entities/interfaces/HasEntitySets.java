package com.org.simplelab.database.entities.interfaces;

/**
 * Denotes that an object contains Entity Sets which must be
 * nullified prior to deletion.
 */
public interface HasEntitySets {

    /**
     * Sets all Set fields of an entity to null
     */
    public void nullifyEntitySets();
}
