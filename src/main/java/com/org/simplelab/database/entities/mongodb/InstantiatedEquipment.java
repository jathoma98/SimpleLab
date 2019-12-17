package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.entities.sql.AbstractEquipment;
import com.org.simplelab.database.entities.sql.Equipment;
import lombok.Data;

import java.util.List;

/**
 * Represents an Equipment object instantiated within a Do Lab instance.
 * We're ignoring the SQL annotations of the superclass -- this is just being
 * serialized and stored in MongoDB.
 */
@Data
public class InstantiatedEquipment extends AbstractEquipment {

    //x and y positions
    private int x;
    private int y;
    private int curr_temp;
    private int curr_val;


    private List<Mixture> mix_list;
    private float purity;
    private Equipment equipment;

    //html elements
    private String li_elem;
    private String drag_elem;

    @Override
    public boolean equals(Object o){
        if (!InstantiatedEquipment.class.isInstance(o))
            return false;
        InstantiatedEquipment cast = (InstantiatedEquipment)o;
        return super.equals(o) && x == cast.getX() && y == cast.getY();
    }

    @Override
    public int hashCode(){
        return super.hashCode() + Integer.hashCode(x) + Integer.hashCode(y);
    }

    @Override
    public String toString() {return super.toString(); }

}
