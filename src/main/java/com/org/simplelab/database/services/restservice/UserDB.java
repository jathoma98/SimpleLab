package com.org.simplelab.database.services.restservice;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.repositories.sql.UserRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.exception.EntityDBModificationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@Component
@Getter
public class UserDB extends SQLService<User> {

    @Autowired
    private UserRepository repository;

    public static final String USERNAME_TAKEN = "username taken";

    public class UserInsertionException extends EntityDBModificationException {
        UserInsertionException(String msg){
            super(msg);
        }
    }

    /**
     * Cannot be used as a username for registered students.
     */
    private static String[] reserved = {
            "Anonymous"
    };

    public enum UserAuthenticationStatus{
        SUCCESSFUL,
        FAILED
    };

    /**
     * Attempts to authenticate a user by their given username and password.
     * @param username - username of the user
     * @param password - raw string password of the user
     * @return - SUCCESSFUL if authentication is successful
     *           FAILED otherwise
     */
    public UserAuthenticationStatus authenticate(String username, String password){
        byte[] given_hashed = DBUtils.getHash(password);
        User found = findUser(username);
        if (found == null)
            return UserAuthenticationStatus.FAILED;
        if (!Arrays.equals(found.getPass_hash(), given_hashed))
            return UserAuthenticationStatus.FAILED;
        return UserAuthenticationStatus.SUCCESSFUL;
    }

    public boolean deleteById(long id) {
        return super.deleteById(id);
    }

    public boolean update(User toUpdate) throws EntityDBModificationException {
        return super.update(toUpdate);
    }

    public User findById(long id) {
        return super.findById(id);
    }

    /**
     * Finds a User in the DB with the given username
     * @param username - username of the user to be found
     * @return A User object if the user exists, null otherwise
     */
    public User findUser(String username){
        List<User> found = repository.findByUsername(username);
        if (found.size() == 0)
            return null;
        return found.get(0);
    }


    /**
     * Inserts the given User object into the DB
     * @param user - User object representing user info
     * @return - True if insertion was successful
     *           False if insertion failed.
     *           Insertion can fail if: The username already exists
     *                                  or
     *                                  The username is reserved
     */
    @Override
    public User insert(User user) throws UserInsertionException{

        if (findUser(user.getUsername()) != null
                || isReserved(user.getUsername()))
            throw new UserInsertionException(USERNAME_TAKEN);
        return repository.save(user);
    }


    /**
     * Deletes the user from the DB, given a User object or a username String.
     * @param user Can be a User object or a String username.
     */
    public void deleteUser(User user){
        deleteUser(user.getUsername());
    }

    public void deleteUser(String username){
        repository.deleteByUsername(username);
    }

    public List<User> searchUserWithKeyword(String keyword) {
        return repository.searchUserWithKeyword(keyword);
    }

    private boolean isReserved(String username){
        List<String> res = Arrays.asList(reserved);
        return res.contains(username);
    }





}