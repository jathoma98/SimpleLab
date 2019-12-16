package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.exception.InvalidFieldException;
import lombok.Data;

@Data
public class StepValidator  extends Validator<Step> {
    private Lab lab;
    private int stepNum;
    private Equipment targetObject;
    private String targetTemperature;
    private String targetVolume;
    private String targetWeight;

    @Override
    public void validate() throws InvalidFieldException {
        StringBuilder sb = new StringBuilder();
        if (targetObject == null) {
            sb.append("invalid target equipment\n");
        }
        if (sb.length() > 0)
            throw new InvalidFieldException(sb.toString());
    }

    @Override
    public Step build() {
        Step step = new Step();
        step.setLab(lab);
        step.setStepNum(stepNum);
        step.setTargetObject(targetObject);
        step.setTargetTemperature(targetTemperature);
        step.setTargetVolume(targetVolume);
        step.setTargetWeight(targetWeight);
        return step;
    }

}
