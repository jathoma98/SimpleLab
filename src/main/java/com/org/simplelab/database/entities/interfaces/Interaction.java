package com.org.simplelab.database.entities.interfaces;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.EquipmentProperty;

/**
 * Base class for Equipment interactions with other equipment.
 */
public interface Interaction {
    Interaction DO_NOTHING = new DoNothing();
    Interaction HEAT = new Heat();

    /**
     * Defines how this Equipment will interact with another target equipment
     * ex: let liquidBeaker = Beaker with liquid inside,
     *         emptyBeaker = Beaker with no liquid inside.
     *
     *     User drags liquidBeaker onto emptyBeaker and clicks -->
     *     Java performs: liquidBeaker.getInteraction().interactWith(emptyBeaker)
     *     which will define the interaction of pouring the liquid into emptyBeaker,
     *     returning the result of the interaction.
     *
     *     We will determine the interaction of Equipment based on the "type" field.
     *     Ex: if an Equipment has "container" type, we will assign the Interaction interface
     *     to the object when we load it in java based on this field.
     *     Look at @PostLoad annotated method in Equipment.
     *
     *     TODO: Discuss: The current plan is not to save these results in MySQL.
     *     MySQL equipment is only for equipment that Teachers make.
     *     My intention is to save all doLab data in a MongoDB document
     *     for each user. We will update this document every thing user makes a move, so we can save user progress
     *     and look at where user makes mistakes. MongoDB is good for this because we dont need to maintain references
     *     or cascade deletes or anything,
     *     we can just save all data in one big document.
     * @param target - The object that this Equipment will interact with
     * @param <T> - Equipment, or subclass of Equipment.
     * @return - The result of this interaction, or Equipment.NO_EQUIPMENT if there is no result.
     */
    <T extends Equipment> T interactWith(T target);

    /**
     * @return the type argument that corresponds to this Interaction.
     */
    String getTypeCode();

    class DoNothing implements Interaction{
        @Override
        public Equipment interactWith(Equipment target) {
            return Equipment.NO_EQUIPMENT;
        }
        @Override
        public String getTypeCode() {
            return "nothing";
        }
    }
    class Heat implements Interaction{
        @Override
        public Equipment interactWith(Equipment target) {
            EquipmentProperty temperatureProperty = target.findProperty("temperature");
            if (temperatureProperty.exists()){
                String newTemp = Integer.toString(Integer.parseInt(temperatureProperty.getPropertyValue()) + 100);
                temperatureProperty.setPropertyValue(newTemp);
            }
            return target;
        }
        @Override
        public String getTypeCode() {
            return "heating";
        }
    }

}
