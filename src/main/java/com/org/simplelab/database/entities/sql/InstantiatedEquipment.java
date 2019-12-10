package com.org.simplelab.database.entities.sql;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents an Equipment object instantiated within a Do Lab instance.
 */
@Data
@Entity(name = DBUtils.INSTANTIATED_EQUIPMENT_TABLE_NAME)
@Table(name = DBUtils.INSTANTIATED_EQUIPMENT_TABLE_NAME)
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
