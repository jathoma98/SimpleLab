package com.org.simplelab.database.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.org.simplelab.database.DBManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DBManager.USER_DOCUMENT_NAME)
public class UserSQL extends BaseTable{

    private String username;
    private byte[] pass_hash;
    private String firstname;
    private String lastname;
    private String institution;
    private String question;
    private byte[] answer;
    private String role;
    private String email;

}
