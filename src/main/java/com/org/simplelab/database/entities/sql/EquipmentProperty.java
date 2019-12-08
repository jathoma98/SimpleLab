package com.org.simplelab.database.entities.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.*;

/*
* This class use to store default property for each equipment.
*
* */

@Data
@Entity(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
public class EquipmentProperty extends BaseTable {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    protected Equipment parentEquipment;

    protected String propertyKey;

    protected String propertyValue;


    @Override
    public int hashCode(){
        return propertyKey.hashCode() + propertyValue.hashCode();
    }

    @Override
    public String toString(){
        return "[ id: " + getId() + "| Parent: " + parentEquipment.getName() + "| Key: " + propertyKey + "| Value: " + propertyValue + " ]";
    }

}
