package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.entities.Lab;

@Data
@Entity(name = DBUtils.COURSE_TABLE_NAME)
@Table(name = DBUtils.COURSE_TABLE_NAME)
public class Course extends BaseTable {

    private String course_id;
    private String name;
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST},
                fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private com.org.simplelab.database.entities.User creator;

    @OneToMany(cascade = {CascadeType.PERSIST},
                fetch = FetchType.LAZY)
    private Set<User> students;

    @OneToMany(cascade = {CascadeType.PERSIST},
                fetch = FetchType.LAZY)
    private Set<Lab> labs;

    public Course() {
        students = new LinkedHashSet<>();
        labs = new LinkedHashSet<>();
    }
}
