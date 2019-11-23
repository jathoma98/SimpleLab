package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = DBUtils.LAB_TABLE_NAME)
public class Lab extends BaseTable{

    private String name;

    @OneToMany
    private List<Equipment> equipments;

    public Lab(){
        this.equipments = new ArrayList<>();
    }
}
