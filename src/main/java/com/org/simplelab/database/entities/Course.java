package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private User creator;

    @OneToMany(cascade = {CascadeType.PERSIST},
                fetch = FetchType.LAZY)
    private Set<User> students;

    public Course() {
        students = new HashSet<>();
    }
}

    /**
    @DBRef
    private List<labs> labs;
    private List<User> students;

    public Course(){
        labs = new ArrayList<>();
        students = new ArrayList<>();
    }


}
**/