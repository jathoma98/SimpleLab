package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.exception.InvalidFieldException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.regex.Pattern;

/**
 * Current User field requirements:
 * all fields: cannot be empty
 * userName: length 4-32
 * password: length 4-32
 *
 */

@Getter
@Setter
@ToString
public class UserValidator extends Validator<User> {

    public static final int MIN_FIELD_LENGTH = 4;
    public static final int MAX_FIELD_LENGTH = 32;
    public static final int GLOBAL_MIN_LENGTH = 1;

    public static final String USERNAME_LENGTH_ERROR = "The username must be between " + MIN_FIELD_LENGTH + "-" + MAX_FIELD_LENGTH + " characters in length. \n";
    public static final String PASSWORD_MATCH_ERROR = "The passwords do not match. \n";
    public static final String PASSWORD_LENGTH_ERROR ="The password must be between " + MIN_FIELD_LENGTH + "-" + MAX_FIELD_LENGTH + " characters in length. \n";
    public static final String INVALID_EMAIL = "The email is invalid. \n";


    private String userName;
    private String email;
    private String firstname;
    private String lastname;
    private String sp_password;
    private String sp_re_password;
    private String question;
    private String answer;
    private String identity;
    private String institution;

    public void validate() throws InvalidFieldException {
        StringBuilder errors = new StringBuilder();

        //check min and max length of username
        if (userName.length() < MIN_FIELD_LENGTH ||
                userName.length() > MAX_FIELD_LENGTH){
            errors.append(USERNAME_LENGTH_ERROR);
        }

        //check that passwords match
        if (!sp_password.equals(sp_re_password)){
            errors.append(PASSWORD_MATCH_ERROR);
        }

        //check password length
        if (sp_password.length() < MIN_FIELD_LENGTH ||
                sp_password.length() > MAX_FIELD_LENGTH){
            errors.append(PASSWORD_LENGTH_ERROR);
        }

        //ensure the email is a valid format.
        if (!Pattern.matches("[^@]+@[^\\.]+\\..+", email)){
            errors.append(INVALID_EMAIL);
        }

        //ensure all fields are filled.
        int[] lengths = {email.length(), question.length(), answer.length(), institution.length()};
        for (int length: lengths){
            if (length == 0)
                errors.append(EMPTY_FIELD);
                break;
            }

        if (errors.length() > 0){
            throw new InvalidFieldException(errors.toString());
        }
    }

    public User build(){

        ModelMapper mm = new ModelMapper();
        User user = mm.map(this, User.class);
        user.setRole(identity);
        user.setPassword(sp_password);

        return user;
    };

}
