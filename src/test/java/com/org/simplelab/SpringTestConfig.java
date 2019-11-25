package com.org.simplelab;

import com.org.simplelab.database.services.UserDB;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.EquipmentRepository;
import com.org.simplelab.database.repositories.LabRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * IMPORTANT NOTE ON WRITING TESTS:
 * TESTS DO NOT RUN IN ORDER OF DECLARATION. Refer to @TestMethodOrder for the order in which
 * tests will run.
 *
 * Notes on DB testing:
 * When writing tests that add entities to the DB, make sure the _metadata field has some value that you can
 * query to delete all the entities you added. This can be done in the cleanup() method.
 * Refer to User_insertionTest() in DBTests.java for an example of this.
 */

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@Transactional
@SpringBootTest
public abstract class SpringTestConfig {

    @Autowired
    UserDB userDB;

    @Autowired
    CourseRepository cr;

    @Autowired
    EquipmentRepository er;

    @Autowired
    LabRepository lr;

    /**
     * Notes on metadata:
     * This metadata field is used to identify entities created during a specific test instance
     * You should modify the zzzzz_cleanup method if you are inserting new entries into different DBs during tests.
     */
    static String metadata;

    @AfterEach
    void clear(){
        userDB.deleteByMetadata(metadata);
        cr.deleteBy_metadata(metadata);
        er.deleteBy_metadata(metadata);
        lr.deleteBy_metadata(metadata);
    }

    @BeforeAll
    static void generateMetadata(){
        int length = 25;
        boolean useLetters = true;
        boolean useNumbers = true;
        metadata = RandomStringUtils.random(length, useLetters, useNumbers);
    }





}
