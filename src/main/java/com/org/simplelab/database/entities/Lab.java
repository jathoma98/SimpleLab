package com.org.simplelab.database.entities;

import java.util.List;

public class Lab {
    String ulid;
    String name;
    String description;
    String own_uuid;
    List<String> list_ueid;
    List<String> list_uuid;

    public Lab(){};

    public Lab(String ulid, String name, String description, String own_uuid, List<String> list_ueid, List<String> list_uuid) {
        this.ulid = ulid;
        this.name = name;
        this.description = description;
        this.own_uuid = own_uuid;
        this.list_ueid = list_ueid;
        this.list_uuid = list_uuid;
    }

    public String getUlid() {
        return ulid;
    }

    public void setUlid(String ulid) {
        this.ulid = ulid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwn_uuid() {
        return own_uuid;
    }

    public void setOwn_uuid(String own_uuid) {
        this.own_uuid = own_uuid;
    }

    public List<String> getList_ueid() {
        return list_ueid;
    }

    public void setList_ueid(List<String> list_ueid) {
        this.list_ueid = list_ueid;
    }

    public List<String> getList_uuid() {
        return list_uuid;
    }

    public void setList_uuid(List<String> list_uuid) {
        this.list_uuid = list_uuid;
    }
}
