package com.org.simplelab.database.entities;
import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = DBManager.COURSE_DOCUMENT_NAME)
public class Course {

    //internal use
    @Id
    private String _id;

    private String course_id;
    private String name;

    //annotation causes MongoDB to store IDs of labs here.
    @DBRef
    private List<Lab> labs;

}
