package com.org.simplelab.database.entities.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Equipment extends AbstractEquipment implements UserCreated {
    private static final Interaction[] interactions = {Interaction.DO_NOTHING, Interaction.HEAT};

    @JsonIgnore
    @OneToMany(mappedBy = "equipmentOne", orphanRemoval = true)
    private List<Recipe> equipmentOne;

    @JsonIgnore
    @OneToMany(mappedBy = "equipmentTwo", orphanRemoval = true)
    private List<Recipe> equipmentTwo;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
                mappedBy = "equipments")
    private Set<Lab> lab;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}
            ,fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    //defines the way this object interacts with another object.
    @Transient
    private Interaction interaction;

    public Equipment(){
        super();
        this.lab = new HashSet<>();
        this.equipmentOne = new ArrayList<>();
        this.equipmentTwo = new ArrayList<>();
    }

//    private String image;

    @PreRemove
    @Transactional
    public void removeFromParents(){
        Collection<Lab> parents = this.getLab();
        parents.forEach( (parent) -> {
            if (parent.getEquipments() != null)
                parent.getEquipments().remove(this);
        });

        System.out.println("E1 parents: " +  getEquipmentOne().toString() );
        System.out.println("E2 parents: " + getEquipmentTwo().toString());
        getEquipmentOne().clear();
        getEquipmentTwo().clear();
    }

    /**
     * Look at the "type" field and set the interaction interface based on its value.
     * the @PostLoad annotation means the method is run automatically when we pull from DB. **/
    @PostLoad
    public void loadInteraction(){
        if (getType() == null){
            this.setInteraction(Interaction.DO_NOTHING);
            return;
        }
        List<Interaction> assigned_interaction = Arrays.stream(interactions)
                                                .filter(inter -> getType().equals(inter.getTypeCode()))
                                                .collect(Collectors.toList());
        if (assigned_interaction.size() > 0)
            this.setInteraction(assigned_interaction.get(0));
        else
            this.setInteraction(Interaction.DO_NOTHING);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o){
        return super.equals(o);
    }

    @Override
    public String toString(){
        return super.toString();
    }


}
