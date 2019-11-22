package com.org.simplelab.database.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.org.simplelab.database.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    public List<User> findByUsername(String username);

    public void deleteByUsername(String username);

    @Query(value = "DELETE FROM user WHERE _metadata = ?1", delete = true)
    public void deleteBy_metadata(String metadata);


}
