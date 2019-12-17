package com.org.simplelab.database.entities.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.Equipment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StepRecord {

    protected int stepNum;
    protected String targetName;
    protected String targetTemperature;
    protected String targetTips;
    protected String targetVolume;
    protected String targetWeight;

    @JsonIgnore
    protected byte[] targetObjectSerial;

    private List<StepAction> userActions;

    @JsonProperty(value = "isComplete")
    private boolean isComplete;

    public StepRecord(){
        this.userActions = new ArrayList<>();
    }

    public void setTargetObject(Equipment e){
        this.targetObjectSerial = DBUtils.serialize(e);
    }


}
