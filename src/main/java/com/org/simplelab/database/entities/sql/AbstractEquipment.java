package com.org.simplelab.database.entities.sql;

import com.org.simplelab.database.entities.interfaces.Interaction;
import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@MappedSuperclass
public abstract class AbstractEquipment extends BaseTable {
    public static final Equipment NO_EQUIPMENT = GEN_NO_EQUIPMENT();

    protected String name;
    protected String type;

    @OneToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER,
            mappedBy = "parentEquipment")
    protected Set<EquipmentProperty> properties;


    protected AbstractEquipment(){
        if (isNew()){
            this.properties = new HashSet<>();
        }
    }

    //TODO: this should be a map, not a hashset
    public EquipmentProperty findProperty(String key){
        for (EquipmentProperty eqp : this.getProperties()) {
            if (eqp.getPropertyKey().equals(key)) { return eqp; }
        }
        return EquipmentProperty.NO_PROPERTY;
    }

    @Override
    public boolean equals(Object o){
        if (!Equipment.class.isInstance(o))
            return false;
        Equipment cast = (Equipment)o;
        return  this.getType().equals(cast.getType()) &&
                this.getProperties().equals(cast.getProperties());
    }

    @Override
    public int hashCode(){
        return name.hashCode() + type.hashCode() + properties.hashCode();
    }

    private static Equipment GEN_NO_EQUIPMENT(){
        Equipment e = new Equipment();
        e.setId(-1);
        return e;
    }

}
