package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = DBManager.LAB_DOCUMENT_NAME)
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
