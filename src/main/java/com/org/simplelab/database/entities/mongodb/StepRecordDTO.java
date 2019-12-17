package com.org.simplelab.database.entities.mongodb;

import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;

@Data
public class StepRecordDTO extends StepRecord {

    private Object targetObject;

    public Object buildTargetObject(){
        this.targetObject = SerializationUtils.deserialize(targetObjectSerial);
        return this.targetObject;
    }

}
