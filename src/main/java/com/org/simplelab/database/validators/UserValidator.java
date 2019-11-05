package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.User;
import lombok.Setter;
import lombok.Getter;

/**
 * Current User field requirements:
 * all fields: cannot be empty
 * userName: length 8-32
 * password: length 8-32
 *
 */

@Getter
@Setter
public class UserValidator extends Validator {

    public static final int MIN_FIELD_LENGTH = 8;
    public static final int MAX_FIELD_LENGTH = 32;
    public static final int GLOBAL_MIN_LENGTH = 1;

    public static final String USERNAME_LENGTH_ERROR = "The username must be between " + MIN_FIELD_LENGTH + "-" + MAX_FIELD_LENGTH + " characters in length. \n";
    public static final String PASSWORD_MATCH_ERROR = "The passwords do not match. \n";
    public static final String PASSWORD_LENGTH_ERROR ="The password must be between " + MIN_FIELD_LENGTH + "-" + MAX_FIELD_LENGTH + " characters in length. \n";
    public static final String EMPTY_FIELD = "Fields cannot be empty. \n";


    private String userName;
    private String email;
    private String sp_password;
    private String sp_re_password;
    private String question;
    private String answer;
    private String identity;

    private User user;

    public void validate() throws InvalidFieldException{
        StringBuilder errors = new StringBuilder();

        if (userName.length() < MIN_FIELD_LENGTH ||
                userName.length() > MAX_FIELD_LENGTH){
            errors.append(USERNAME_LENGTH_ERROR);
        }

        if (!sp_password.equals(sp_re_password)){
            errors.append(PASSWORD_MATCH_ERROR);
        }

        if (sp_password.length() < MIN_FIELD_LENGTH ||
                sp_password.length() > MAX_FIELD_LENGTH){
            errors.append(PASSWORD_LENGTH_ERROR);
        }

        int[] lengths = {email.length(), question.length(), answer.length()};
        for (int length: lengths){
            if (length < GLOBAL_MIN_LENGTH){
                errors.append(EMPTY_FIELD);
                break;
            }
        }

        if (errors.length() > 0){
            throw new InvalidFieldException(errors.toString());
        }
    }

    public User build(){
        User user = new User();
        user.setUsername(userName);
        user.setPassword(sp_password);
        user.setEmail(email);
        user.setQuestion(question);
        user.setAnswer(answer);
        user.setRole(identity);
        return user;
    };

}
