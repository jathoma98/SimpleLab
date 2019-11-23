package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = DBUtils.COURSE_TABLE_NAME)
public class Course extends BaseTable {

    private String course_id;
    private String name;
    private String description;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany
    private List<Lab> labs;
    @OneToMany
    private List<User> users;

    public Course(){
        labs = new ArrayList<>();
        users = new ArrayList<>();
    }
}
