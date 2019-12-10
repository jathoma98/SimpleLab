package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.AbstractEquipment;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

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

    @Override
    public boolean equals(Object o){
        return super.equals(o);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }

    @Override
    public String toString() {return super.toString(); }

}
