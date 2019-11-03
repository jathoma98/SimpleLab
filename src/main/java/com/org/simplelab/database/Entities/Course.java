package com.org.simplelab.database.Entities;
import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    String id;
    String name;
    String create_day;

    public Course (String id, String name, String create_day){
        this.id = id;
        this.name = name;
        this.create_day = create_day;
    }

    public String getCreate_day() {
        return create_day;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setCreate_day(String create_day) {
        this.create_day = create_day;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
