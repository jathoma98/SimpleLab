package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = DBManager.COURSE_DOCUMENT_NAME)
public class CourseSQL extends BaseTable {

    private String course_id;
    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private User creator;


}
