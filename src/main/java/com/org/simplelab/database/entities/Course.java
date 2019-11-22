package com.org.simplelab.database.entities;
import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = DBManager.COURSE_DOCUMENT_NAME)
public class Course extends BaseDocument {

    private String course_id;
    private String name;
    private String description;
    private User creator;

    //annotation causes MongoDB to store IDs of labs here.
    @DBRef
    private List<Lab> labs;

    @DBRef
    private List<User> users;

    public Course(){
        labs = new ArrayList<>();
        users = new ArrayList<>();
    }

}
