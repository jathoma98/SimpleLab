package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity(name = DBUtils.EQUIPMENT_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_TABLE_NAME)
public class Equipment extends BaseTable implements UserCreated {

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator;

    private String description;

    //properties cannot exist without a parent equipment,
    //so we cascade remove
    @OneToMany(cascade = {CascadeType.PERSIST,
                          CascadeType.REMOVE},
                fetch = FetchType.EAGER,
                mappedBy = "parentEquipment")
    private Set<EquipmentProperty> properties;

    public Equipment(){
        if (isNew()){
            this.properties = new HashSet<>();
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode() + creator.hashCode() + description.hashCode() + properties.hashCode();
    }


}
