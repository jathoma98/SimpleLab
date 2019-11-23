package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity(name = DBUtils.LAB_TABLE_NAME)
@Table(name = DBUtils.LAB_TABLE_NAME)
public class Lab extends BaseTable{

    private String name;

    /**
    @DBRef
    private List<Equipment> equipment;

    public Lab() {
        equipment = new ArrayList<>();
    }
    **/

}
