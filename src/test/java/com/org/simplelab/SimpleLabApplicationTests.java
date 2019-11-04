package com.org.simplelab;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.commons.lang3.RandomStringUtils;

@SpringBootTest
class SimpleLabApplicationTests {

	@Autowired
	UserDB userDB;

	static String metadata;

	@BeforeAll
	static void generateMetadata(){
		int length = 256;
		boolean useLetters = true;
		boolean useNumbers = true;
		metadata = RandomStringUtils.random(length, useLetters, useNumbers);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void insertionTest(){

		User user = new User();
		StringBuilder sb = new StringBuilder();
		sb.append("test user -- ").append(metadata);

		user.setUsername(sb.toString());
		user.setPassword("a password to be hashed");
		user._metadata = metadata;
		userDB.insertUser(user);

	}

	@Test
	void cleanup(){

		//find all users with matching metadata and delete
		userDB.deleteByMetadata(metadata);
	}


}
