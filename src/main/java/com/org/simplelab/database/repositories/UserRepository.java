package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface UserRepository extends BaseRepository<User> {


    public List<User> findByUsername(String username);

    @Modifying
    @Transactional
    public void deleteByUsername(String username);


//    @Query(nativeQuery = true, value = "SELECT id, username, created_date, email, firstname, lastname, role  " +
//            "FROM simplelab.user " +
//            "WHERE username OR institution OR email OR firstname OR lastname LIKE '%:keyword%'")
    @Query(nativeQuery = true, value =
            "SELECT * FROM simplelab.user " +
            "WHERE  username LIKE '%:keyword%' OR institution LIKE '%:keyword%'")
    public List<User> searchUserWithKeyword(@Param("keyword") String keyword);


}
