package com.org.simplelab.database.repositories;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserRepository extends BaseRepository<User> {


    public List<User> findByUsername(String username);

    @Modifying
    public void deleteByUsername(String username);



}
