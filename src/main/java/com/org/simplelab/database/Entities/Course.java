package com.org.simplelab.database.Entities;
import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    String ucid;
    String name;
    String create_day;
    String own_uuid;
    List<String> list_of_uuid; //Student ID
    List<String> list_of_ulid; //Lab ID

    public Course() {}

    public Course(String ucid, String name, String create_day, String own_uuid, List<String> list_of_uuid, List<String> list_of_ulid) {
        this.ucid = ucid;
        this.name = name;
        this.create_day = create_day;
        this.own_uuid = own_uuid;
        this.list_of_uuid = list_of_uuid;
        this.list_of_ulid = list_of_ulid;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_day() {
        return create_day;
    }

    public void setCreate_day(String create_day) {
        this.create_day = create_day;
    }

    public String getOwn_uuid() {
        return own_uuid;
    }

    public void setOwn_uuid(String own_uuid) {
        this.own_uuid = own_uuid;
    }

    public List<String> getList_of_uuid() {
        return list_of_uuid;
    }

    public void setList_of_uuid(List<String> list_of_uuid) {
        this.list_of_uuid = list_of_uuid;
    }

    public List<String> getList_of_ulid() {
        return list_of_ulid;
    }

    public void setList_of_ulid(List<String> list_of_ulid) {
        this.list_of_ulid = list_of_ulid;
    }

    @Override
    public String toString() {
        return "Course{" +
                "ucid='" + ucid + '\'' +
                ", name='" + name + '\'' +
                ", create_day='" + create_day + '\'' +
                ", own_uuid='" + own_uuid + '\'' +
                ", list_of_uuid=" + list_of_uuid +
                ", list_of_ulid=" + list_of_ulid +
                '}';
    }
}
