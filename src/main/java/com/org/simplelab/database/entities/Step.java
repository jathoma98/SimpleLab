package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = DBUtils.STEP_TABLE_NAME)
@Table(name = DBUtils.STEP_TABLE_NAME)
public class Step extends BaseTable implements UserCreated{

    @OneToOne(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
    private int stepNum;

    @ManyToOne
    private Lab lab;

    @OneToOne(cascade = {CascadeType.PERSIST})
    private Equipment targetObject;

    @Override
    public String toString(){
        return "[id: " + getId() + "| lab: " + getLab().getName() + "| step: " + stepNum + "| target: " + targetObject.getName() + "]";
    }

}
