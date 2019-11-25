package com.org.simplelab.database.services;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Component
public class UserDB extends DBService<User>{

    public static final String USERNAME_TAKEN = "username taken";

    public class UserInsertionException extends DBService.EntityInsertionException {
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

    public enum UserInsertionStatus{
        SUCCESSFUL,
        FAILED
    }

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

    /**
     * Finds a User in the DB with the given username
     * @param username - username of the user to be found
     * @return A User object if the user exists, null otherwise
     */
    public User findUser(String username){
        List<User> found = userRepository.findByUsername(username);
        if (found.size() == 0)
            return null;
        return found.get(0);
    }

    @Override
    public User findById(long id){
        Optional<User> found = userRepository.findById(id);
        return found.isPresent() ? found.get(): null;
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
    public boolean insert(User user) throws UserInsertionException{

        if (findUser(user.getUsername()) != null
                || isReserved(user.getUsername()))
            throw new UserInsertionException(USERNAME_TAKEN);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deleteById(long id){
        userRepository.deleteById(id);
        return true;
    }

    /**
     * Updates the corresponding user in the DB given a representative User object.
     * @param user - User object representing the User to be updated.
     */
    @Override
    public boolean update(User user){
        userRepository.save(user);
        return true;
    }

    /**
     * Deletes the user from the DB, given a User object or a username String.
     * @param user Can be a User object or a String username.
     */
    public void deleteUser(User user){
        deleteUser(user.getUsername());
    }


    public void deleteByMetadata(String metadata){
        userRepository.deleteBy_metadata(metadata);
    }

    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }

    public List<User> searchUserWithKeyword(String keyword) {
        return userRepository.searchUserWithKeyword(keyword);
    }





    public UserRepository DEBUG_getInterface(){
        return userRepository;
    }

    private boolean isReserved(String username){
        List<String> res = Arrays.asList(reserved);
        return res.contains(username);
    }





}