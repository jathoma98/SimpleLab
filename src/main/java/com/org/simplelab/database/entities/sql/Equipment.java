package com.org.simplelab.database.entities.sql;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.Interaction;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Data
@Entity(name = DBUtils.EQUIPMENT_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Equipment extends BaseTable implements UserCreated {
    private static final Interaction[] interactions = {Interaction.DO_NOTHING, Interaction.HEAT};
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
        List<Interaction> assigned_interaction = Arrays.stream(interactions)
                                                .filter(inter -> getType().equals(inter.getTypeCode()))
                                                .collect(Collectors.toList());
        if (assigned_interaction.size() > 0)
            this.setInteraction(assigned_interaction.get(0));
        else
            this.setInteraction(Interaction.DO_NOTHING);
    }

    public Equipment(){
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
        return name.hashCode() + creator.hashCode() + type.hashCode() + properties.hashCode();
    }

    private static Equipment GEN_NO_EQUIPMENT(){
        Equipment e = new Equipment();
        e.setId(-1);
        return e;
    }


}
