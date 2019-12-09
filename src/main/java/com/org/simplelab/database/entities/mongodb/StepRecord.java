package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.entities.sql.Step;
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
