package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = DBManager.EQUIPMENT_DOCUMENT_NAME)
public class Equipment extends BaseDocument {

    private String name;
    private User creator;

}
