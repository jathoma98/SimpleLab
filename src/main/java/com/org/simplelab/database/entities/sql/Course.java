package com.org.simplelab.database.entities.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = DBUtils.COURSE_TABLE_NAME)
@Table(name = DBUtils.COURSE_TABLE_NAME)
public class Course extends BaseTable implements UserCreated{

    @Column(unique = true)
    private String course_id;

    private String name;
    private String description;
    private String invite_code;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private Set<User> students;

    @ManyToMany(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "labs")
    private Set<Lab> labs;

    public Course() {
        students = new HashSet<>();
        labs = new HashSet<>();
    }

}