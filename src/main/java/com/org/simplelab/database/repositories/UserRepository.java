package com.org.simplelab.database.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.org.simplelab.database.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

    public List<User> findByUsername(String username);

    public void deleteByUsername(String username);

    @Query(value = "{ _metadata: ?0 }", delete = true)
    public List<User> deleteByMetadata(String metadata);

}
