package com.org.simplelab;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleLabApplicationTests {

	@Autowired
	UserDB userDB;

	@Test
	void contextLoads() {
	}

	@Test
	void insertionTest(){

		User user = new User();
		user.setUsername("the first test user");
		user.setPassword("a password to be hashed");
		userDB.insertUser(user);

	}


}
