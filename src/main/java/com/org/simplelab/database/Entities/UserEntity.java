package com.org.simplelab.database.Entities;

import java.io.Serializable;

class Role{
    static final public int TEACHER = 1;
    static final public int STUDENT = 2;
}

public class UserEntity implements Serializable {
    String uuid;
    String username;
    String fisrtname;
    String lastname;
    String institution;
    String question;
    String anwser;
    int role;

    public UserEntity(){};
    public UserEntity(String uuid, String username, String fisrtname, String lastname, String institution, String question, String anwser, int role) {
        this.uuid = uuid;
        this.username = username;
        this.fisrtname = fisrtname;
        this.lastname = lastname;
        this.institution = institution;
        this.question = question;
        this.anwser = anwser;
        this.role = role;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFisrtname() {
        return fisrtname;
    }

    public void setFisrtname(String fisrtname) {
        this.fisrtname = fisrtname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnwser() {
        return anwser;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }

    public void setRole(int role) {
        this.role = role;
    }
}

