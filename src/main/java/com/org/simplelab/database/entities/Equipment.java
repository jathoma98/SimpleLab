package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Data
@Entity(name = DBUtils.EQUIPMENT_TABLE_NAME)
@Table(name = DBUtils.EQUIPMENT_TABLE_NAME)
public class Equipment extends BaseTable implements UserCreated {

    private String name;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;

}
