package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
public class EquipmentProperty extends BaseTable {

    @ManyToOne(fetch = FetchType.EAGER)
    Equipment parentEquipment;

    private String propertyKey;

    private String propertyValue;


    @Override
    public int hashCode(){
        return propertyKey.hashCode() + propertyValue.hashCode();
    }

    @Override
    public String toString(){
        return "[ id: " + getId() + "| Parent: " + parentEquipment.getName() + "| Key: " + propertyKey + "| Value: " + propertyValue + " ]";
    }

}
