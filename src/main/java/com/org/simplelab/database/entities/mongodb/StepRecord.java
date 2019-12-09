package com.org.simplelab.database.entities.mongodb;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StepRecord {

    private int stepNum;

    private List<StepAction> userActions;

    public StepRecord(){
        this.userActions = new ArrayList<>();
    }

}
