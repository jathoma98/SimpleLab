package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.Equipment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StepRecord {

    private int stepNum;
    private String targetName;
    private String targetTemperature;
    private String targetTips;
    private String targetVolume;
    private String targetWeight;
    private byte[] targetObject; //serialized

    private List<StepAction> userActions;
    private boolean isComplete;

    public StepRecord(){
        this.userActions = new ArrayList<>();
    }

    public void setTargetObject(Equipment e){
        this.targetObject = DBUtils.serialize(e);
    }

    public Equipment buildTargetObject(){
        return DBUtils.deserialize(this.getTargetObject());
    }

}
