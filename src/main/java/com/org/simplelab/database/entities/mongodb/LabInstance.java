package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.DBUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@Document(collection = DBUtils.LABINSTANCE_DOCUMENT_NAME)
public class LabInstance extends BaseDocument{
    public static final LabInstance NO_INSTANCE = GEN_NO_INSTANCE();

    //id of user doing lab
    private long userId;

    //id of lab being done
    private long labId;

    //whether the lab is finished or not
    private boolean finished = false;

    //grade for user
    private String grade = "unfinished";

    //save original lab here
    private byte[] serialized_lab;

    private List<StepRecord> stepRecords;

    public LabInstance(){
        super();
        this.stepRecords = new ArrayList<>();
    }

    private static LabInstance GEN_NO_INSTANCE(){
        LabInstance none = new LabInstance();
        none.set_id(NOT_FOUND_KEY);
        return none;
    }

}
