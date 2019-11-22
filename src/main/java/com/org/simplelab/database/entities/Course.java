package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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


}
