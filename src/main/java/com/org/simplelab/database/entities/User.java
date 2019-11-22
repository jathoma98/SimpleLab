package com.org.simplelab.database.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DBManager.USER_DOCUMENT_NAME)
public class User extends BaseTable{

    private String username;
    private byte[] pass_hash;
    private String firstname;
    private String lastname;
    private String institution;
    private String question;
    private byte[] answer;
    private String role;
    private String email;

    /**
     * Use this method to set passwords.
     * @param password - the password to be hashed and stored.
     */
    public void setPassword(String password){
        this.setPass_hash(DBManager.getHash(password));
    }

    public void setAnswer(String answer) {
        this.answer = DBManager.getHash(answer);
    }

}
