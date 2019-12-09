package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.DBUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = DBUtils.LABINSTANCE_DOCUMENT_NAME)
public class LabInstance extends BaseDocument {

    private String test;

}
