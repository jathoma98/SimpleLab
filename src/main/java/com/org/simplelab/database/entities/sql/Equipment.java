package com.org.simplelab.database.entities.sql;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.Interaction;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity(name = DBUtils.EQUIPMENT_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_TABLE_NAME)
public class Equipment extends BaseTable implements UserCreated {
    public static final Equipment NO_EQUIPMENT = GEN_NO_EQUIPMENT();

    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}
            ,fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    private String type;

    //defines the way this object interacts with another object.
    @Transient
    private Interaction interaction;

    @OneToMany(cascade = {CascadeType.ALL},
                fetch = FetchType.EAGER,
                mappedBy = "parentEquipment")
    private Set<EquipmentProperty> properties;

    /**
     * Look at the "type" field and set the interaction interface based on its value.
     * the @PostLoad annotation means the method is run automatically when we pull from DB.
     */
    @PostLoad
    public void loadInteraction(){
        //TODO: implement this
        setInteraction(Interaction.DO_NOTHING);
    }

    public Equipment(){
        if (isNew()){
            this.properties = new HashSet<>();
        }
    }

    //TODO: this should be a map, not a hashset
    public EquipmentProperty findProperty(String key){
        EquipmentProperty toFind = new EquipmentProperty();
        toFind.setPropertyKey(key);
        if (this.getProperties().contains(toFind)) {
            for (EquipmentProperty eqp : this.getProperties()) {
                if (eqp.getPropertyKey().equals(key)) {
                    return eqp;
                }
            }
        }
        return EquipmentProperty.NO_PROPERTY;
    }

    @Override
    public int hashCode(){
        return name.hashCode() + creator.hashCode() + type.hashCode() + properties.hashCode();
    }

    private static Equipment GEN_NO_EQUIPMENT(){
        Equipment e = new Equipment();
        e.setId(-1);
        return e;
    }


}
