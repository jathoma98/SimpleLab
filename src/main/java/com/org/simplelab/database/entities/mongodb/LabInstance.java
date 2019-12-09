package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.DBUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = DBUtils.LABINSTANCE_DOCUMENT_NAME)
public class LabInstance extends BaseDocument {

    //save original lab here
    private byte[] serialized_lab;

    private List<StepRecord> stepRecords;

}
