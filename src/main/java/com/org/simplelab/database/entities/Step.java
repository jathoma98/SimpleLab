package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity(name = DBUtils.STEP_TABLE_NAME)
@Table(name = DBUtils.STEP_TABLE_NAME)
public class Step extends BaseTable implements UserCreated{

    @OneToOne
    private User creator;
    private int stepNum;
    private Lab lab;

    @ManyToOne
    private Equipment targetObject;

    @Override
    public String toString(){
        return "[id: " + getId() + "| lab: " + getLab() + "| step: " + stepNum + "]";
    }

}
