package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.HasEntitySets;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = DBUtils.COURSE_TABLE_NAME)
@Table(name = DBUtils.COURSE_TABLE_NAME)
public class Course extends BaseTable implements UserCreated, HasEntitySets{

    @Column(unique = true)
    private String course_id;
    private String name;
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<User> students;

    @ManyToMany(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<Lab> labs;

    public Course() {
        students = new HashSet<>();
        labs = new HashSet<>();
    }

    @Override
    public void nullifyEntitySets() {
        setStudents(null);
        setLabs(null);
    }
}