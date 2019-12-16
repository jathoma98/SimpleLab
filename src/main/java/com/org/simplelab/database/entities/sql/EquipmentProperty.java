package com.org.simplelab.database.entities.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
* This class use to store default property for each equipment.
*
* */

@Data
@Entity(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
public class EquipmentProperty extends BaseTable {
    public static final EquipmentProperty NO_PROPERTY = GEN_NO_PROPERTY();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    protected Equipment parentEquipment;

    protected String propertyKey;

    protected String propertyValue;


    @Override
    public int hashCode(){
        if (propertyKey == null)
            return 0;
        return propertyKey.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (!EquipmentProperty.class.isInstance(o))
            return false;
        EquipmentProperty cast = (EquipmentProperty)o;
        return  this.getPropertyKey().equals(cast.getPropertyKey())
                && this.getPropertyValue().equals(cast.getPropertyValue());
    }


    @Override
    public String toString(){
        return "Key: " + propertyKey + "| Value: " + propertyValue + " ]";
    }

    private static EquipmentProperty GEN_NO_PROPERTY(){
        EquipmentProperty eqp = new EquipmentProperty();
        eqp.setId(-1);
        return eqp;
    }

}
