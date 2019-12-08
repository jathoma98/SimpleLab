package com.org.simplelab.database.entities.sql;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = DBUtils.LAB_TABLE_NAME)
@Table(name = DBUtils.LAB_TABLE_NAME)
public class Lab extends BaseTable implements UserCreated{

    private String name;
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
               fetch = FetchType.LAZY)
    private Set<Equipment> equipments;

    @OneToMany(cascade = {CascadeType.ALL},
                fetch = FetchType.LAZY,
                mappedBy = "lab")
    private List<Step> steps;

    public Lab(){
        this.equipments = new HashSet<>();
        this.steps = new ArrayList<>();
    }
}
