package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_PROPERTY_TABLE_NAME)
public class EquipmentProperty extends BaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    Equipment parentEquipment;

    private String property_key;

    private String property_value;


    @Override
    public int hashCode(){
        return property_key.hashCode() + property_value.hashCode();
    }

    @Override
    public String toString(){
        return "[ id: " + getId() + " Key: " + property_key + " Value: " + property_value + " ]";
    }

}
