package com.org.simplelab;

import com.org.simplelab.database.repositories.mongodb.LabInstanceRepository;
import com.org.simplelab.database.repositories.sql.CourseRepository;
import com.org.simplelab.database.repositories.sql.EquipmentRepository;
import com.org.simplelab.database.repositories.sql.LabRepository;
import com.org.simplelab.database.services.restservice.*;
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
    protected UserDB userDB;
    @Autowired
    protected CourseRepository cr;
    @Autowired
    protected EquipmentRepository er;
    @Autowired
    protected LabRepository lr;
    @Autowired
    protected CourseDB courseDB;
    @Autowired
    protected LabDB labDB;
    @Autowired
    protected EquipmentDB equipmentDB;
    @Autowired
    protected  LabInstanceRepository lir;
    @Autowired
    protected LabInstanceDB instanceDB;
    @Autowired
    protected ImageFileDB imageDB;


    protected static final long user_id = 26667;
    protected static final String username = "UNITTEST_TEACHER";

    /**
     * Notes on metadata:
     * This metadata field is used to identify entities created during a specific test instance
     * You should modify the zzzzz_cleanup method if you are inserting new entries into different DBs during tests.
     */
    public static String metadata;

    @AfterEach
    void clear(){
        cr.deleteBy_metadata(metadata);
        er.deleteBy_metadata(metadata);
        lr.deleteBy_metadata(metadata);
        lir.deleteBy_metadata(metadata);
    }

    @BeforeAll
    static void generateMetadata(){
        int length = 25;
        boolean useLetters = true;
        boolean useNumbers = true;
        metadata = RandomStringUtils.random(length, useLetters, useNumbers);
    }





}
