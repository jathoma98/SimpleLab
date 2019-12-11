package com.org.simplelab.database.entities.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@Data
@Entity(name = DBUtils.LAB_TABLE_NAME)
@Table(name = DBUtils.LAB_TABLE_NAME)
public class Lab extends BaseTable implements UserCreated{

    private String name;
    private String description;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
                mappedBy = "labs")
    private Set<Course> course;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
               fetch = FetchType.LAZY)
    private Set<Equipment> equipments;

    @OneToMany(cascade = {CascadeType.ALL},
                fetch = FetchType.EAGER,
                mappedBy = "lab",
                orphanRemoval = true)
    private List<Step> steps;

    @Transactional
    @PreRemove
    public void removeFromParents(){
        Collection<Course> parents = this.getCourse();
        parents.forEach( (parent) -> {
            parent.getLabs().remove(this);
        });
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }

    @Override
    public String toString(){
        return "Lab name: " + this.getName() + "\n" +
                "Id: " + getId();
    }


    public Lab(){
        this.equipments = new HashSet<>();
        this.steps = new ArrayList<>();
    }

    public int returnLastStepNumber(){
        return steps.stream()
                    .max(Comparator.comparing(Step::getStepNum))
                    .get().getStepNum();
    }
}
