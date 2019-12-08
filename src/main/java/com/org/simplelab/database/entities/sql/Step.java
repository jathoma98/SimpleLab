package com.org.simplelab.database.entities.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = DBUtils.STEP_TABLE_NAME)
@Table(name = DBUtils.STEP_TABLE_NAME)
public class Step extends BaseTable{

    private int stepNum;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @OneToOne(cascade = {CascadeType.ALL})
    private Equipment targetObject;

    @Override
    public String toString(){
        String tar = targetObject == null? "null": targetObject.getName();
        String labinfo = lab == null? "null": lab.getName();
        return "[id: " + getId() + "| lab: " + labinfo + "| step: " + stepNum + "| target: " + tar + "]";
    }

}
