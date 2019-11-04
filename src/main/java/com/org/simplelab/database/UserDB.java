package com.org.simplelab.database;

import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

@Transactional
@Component
public class UserDB{

    @Autowired
    UserRepository userRepository;

    /**
     * Cannot be used as a username for registered users.
     */
    private static String[] reserved = {
            "Anonymous"
    };

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

    /**
     * Inserts the given User object into the DB
     * @param user - User object representing user info
     * @return - True if insertion was successful
     *           False if insertion failed.
     *           Insertion can fail if: The username already exists
     *                                  or
     *                                  The username is reserved
     */
    public boolean insertUser(User user){

        if (findUser(user.getUsername()) != null || isReserved(user.getUsername()))
            return false;
        userRepository.save(user);
        return true;
    }

    /**
     * Deletes all users which contain the given metadata
     * @param metadata - metadata to be matched
     */
    public void deleteByMetadata(String metadata){
        userRepository.deleteByMetadata(metadata);
    }

    private boolean isReserved(String username){
        List<String> res = Arrays.asList(reserved);
        return res.contains(username);
    }



}