package com.org.simplelab.database.entities;

import com.org.simplelab.database.DBManager;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Arrays;

@Document(collection = DBManager.USER_DOCUMENT_NAME)
public class User implements Serializable {

    @Id
    private String id;

    private String username;
    private byte[] pass_hash;
    private String firstname;
    private String lastname;
    private String institution;
    private String question;
    private byte[] answer;
    private String role;
    private String email;

    //metadata field for interacting with DB tests
    public String _metadata;

    public User() {
    }

    /**
     * Use this method to set passwords.
     * @param password - the password to be hashed and stored.
     */
    public void setPassword(String password){
        this.setPass_hash(DBManager.getHash(password));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPass_hash() {
        return pass_hash;
    }

    private void setPass_hash(byte[] pass_hash) {
        this.pass_hash = pass_hash;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public byte[] getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = DBManager.getHash(answer);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", pass_hash=" + Arrays.toString(pass_hash) +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", institution='" + institution + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

