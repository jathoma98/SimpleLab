package com.org.simplelab.database.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.org.simplelab.database.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

    /**
     * Searches for Users which contain the value 'regex' in the following fields:
     * username, firstname, lastname, institution
     * @param regex - the string to be found
     */
    String REGEX_QUERY = "{ '$or': [ " +
                            "{ username: {'$regex': ?0, '$options': 'i'}}," +
                            "{ firstname: {'$regex': ?0, '$options': 'i'}}," +
                            "{ lastname: {'$regex': ?0, '$options': 'i'}}," +
                            "{ institution: {'$regex': ?0, '$options': 'i'}}" +
                            "]" +
                         "})";
    @Query(value = REGEX_QUERY, fields = "{ 'username': 1, 'firstname': 1, 'lastname': 1, 'institution': 1 }")
    public List<User> findInfoFieldsWithRegex(String regex);

    public List<User> findByUsername(String username);

    public void deleteByUsername(String username);

    @Query(value = "{ _metadata: ?0 }", delete = true)
    public List<User> deleteByMetadata(String metadata);


}
