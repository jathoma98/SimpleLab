package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface UserRepository extends BaseRepository<User> {


    public List<User> findByUsername(String username);

    @Modifying
    @Transactional
    public void deleteByUsername(String username);



}
