package com.org.simplelab.database.entities.mongodb;

import lombok.Data;

/**
 * Records user interactions
 */
@Data
public class StepAction {
    /**
     * toString() method on equipment 1
     */
    private String equipment1ToString;

    /**
     * Interaction.getTypeCode() result
     */
    private String interaction;

    /**
     * Parameters for interaction
     */
    private String parameter;

    /**
     * toString() method on equipment 2
     */
    private String equipment2ToString;

    /**
     * result of interaction
     */
    private String result;
}
