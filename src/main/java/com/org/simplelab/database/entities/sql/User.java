package com.org.simplelab.database.entities.sql;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.org.simplelab.database.DBUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = DBUtils.USER_TABLE_NAME)
@Table(name = DBUtils.USER_TABLE_NAME)
public class User extends BaseTable {

    private String username;

    @JsonIgnore
    private byte[] pass_hash;
    private String firstname;
    private String lastname;
    private String institution;

    @JsonIgnore
    private String question;

    @JsonIgnore
    private byte[] answer;
    private String role;
    private String email;

    /**
     * Use this method to set passwords.
     * @param password - the password to be hashed and stored.
     */
    public void setPassword(String password){
        this.setPass_hash(DBUtils.getHash(password));
    }

    public void setAnswer(String answer) {
        this.answer = DBUtils.getHash(answer);
    }

}
