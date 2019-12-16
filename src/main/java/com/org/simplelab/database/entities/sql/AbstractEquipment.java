package com.org.simplelab.database.entities.sql;

import com.org.simplelab.database.entities.sql.files.ImageFile;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@MappedSuperclass
public abstract class AbstractEquipment extends BaseTable{
    public static final Equipment NO_EQUIPMENT = GEN_NO_EQUIPMENT();

    protected String name;
    protected String type;

    @OneToOne(cascade = {CascadeType.ALL},
              fetch = FetchType.LAZY)
    @JoinColumn(name = "img")
    protected ImageFile img;

    @OneToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER,
            mappedBy = "parentEquipment")
    protected Set<EquipmentProperty> properties;


    protected AbstractEquipment(){
        if (isNew()){
            this.properties = new HashSet<>();
        }
    }

    public EquipmentProperty findProperty(String key){
        for (EquipmentProperty eqp : this.getProperties()) {
            if (eqp.getPropertyKey().equals(key)) { return eqp; }
        }
        return EquipmentProperty.NO_PROPERTY;
    }

    @Override
    public boolean equals(Object o){
        if (!AbstractEquipment.class.isInstance(o))
            return false;
        AbstractEquipment cast = (AbstractEquipment)o;
        return  this.getType().equals(cast.getType()) &&
                this.getProperties().equals(cast.getProperties());
    }

    @Override
    public int hashCode(){
        return name.hashCode() + type.hashCode() + properties.hashCode();
    }

    @Override
    public String toString(){
        return  "Name: " + name + " \n" +
                "Type: " + type + " \n" +
                "Properties: " + properties.toString();
    }

    private static Equipment GEN_NO_EQUIPMENT(){
        Equipment e = new Equipment();
        e.setId(-1);
        return e;
    }

}
