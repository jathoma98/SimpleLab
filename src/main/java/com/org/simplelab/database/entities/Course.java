package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = DBManager.COURSE_DOCUMENT_NAME)
public class Course extends BaseTable {

    private String course_id;
    private String name;
    private String description;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;


}
