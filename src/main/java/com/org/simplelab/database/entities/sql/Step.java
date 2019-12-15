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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE},
    fetch = FetchType.EAGER)
    private Equipment targetObject;

    private String targetTemperature;
    private String targetVolume;
    private String targetWeight;
    private String targetName;
    private String targetTips;

    @Override
    public String toString(){
        String tar = targetObject == null? "null": targetObject.getName();
        String labinfo = lab == null? "null": lab.getName();
        return "[id: " + getId() + "| lab: " + labinfo + "| step: " + stepNum + "| target: " + tar + "]";
    }

    @Override
    public int compareTo(BaseTable s){
        return this.getStepNum() - ((Step)s).getStepNum();
    }

}
