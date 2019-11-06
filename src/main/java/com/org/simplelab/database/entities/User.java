package com.org.simplelab.database.entities;

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

    public void setAnswer(String answer) {
        this.answer = DBManager.getHash(answer);
    }

}

