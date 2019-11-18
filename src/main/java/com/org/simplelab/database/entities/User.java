package com.org.simplelab.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = DBManager.USER_DOCUMENT_NAME)
public class User extends BaseDocument implements Serializable {

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

    public String getId(){
        return get_id();
    }

}

