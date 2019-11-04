package com.org.simplelab;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;

import com.org.simplelab.database.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IMPORTANT NOTE ON WRITING TESTS:
 * TESTS DO NOT RUN IN ORDER OF DECLARATION. Refer to @TestMethodOrder for the order in which
 * tests will run.
 *
 * Notes on DB testing:
 * When writing tests that add entities to the DB, make sure the _metadata field has some value that you can
 * query to delete all the entities you added. This can be done in the cleanup() method.
 * Refer to User_insertionTest() for an example of this.
 */

//This annotation makes tests run in alphabetical order.
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@SpringBootTest
class SimpleLabApplicationTests {

	@Autowired
	UserDB userDB;

	static String metadata;
	static String prefix = "test user -- ";

	@BeforeAll
	static void generateMetadata(){
		int length = 25;
		boolean useLetters = true;
		boolean useNumbers = true;
		metadata = RandomStringUtils.random(length, useLetters, useNumbers);
	}

	@Test
	void aaaaa_contextLoads() {
	}

	@Test
	void User_insertionTest(){

		User user = new User();
		StringBuilder sb = new StringBuilder();
		sb.append(prefix).append(metadata);
		String username = sb.toString();

		System.out.println("Inserting user: " + username);
		user.setUsername(username);
		user.setPassword("a password to be hashed");

		//adding metadata to delete this entity later.
		user._metadata = metadata;
		assertTrue(userDB.insertUser(user));

		//ensure duplicate insertion returns false
		assertFalse(userDB.insertUser(user));

		//ensure duplicate insertion doesn't insert an additional instance with the same username
		UserRepository repo = userDB.DEBUG_getInterface();
		assertEquals(repo.findByUsername(user.getUsername()).size(), 1);

	}

	@Test
	void User_retrievalTest(){
		StringBuilder sb = new StringBuilder();
		String username = sb.append("Another test user -- ").append(metadata).toString();

		System.out.println("Retrieving user: " + username);
		User user = new User();
		user.setUsername(username);
		user.setPassword("passy pass");
		user._metadata = metadata;

		userDB.insertUser(user);
		User found = userDB.findUser(username);
		assertNotNull(found);
		assertEquals(found.getUsername(), username);
	}

	@Test
	void User_deletionTest(){
		StringBuilder sb = new StringBuilder();
		String username = sb.append("Test user to delete -- ").append(metadata).toString();

		User user = new User();
		user._metadata = metadata;
		user.setUsername(username);
		user.setPassword("a pass");
		userDB.insertUser(user);

		userDB.deleteUser(user);
		assertNull(userDB.findUser(user.getUsername()));

	}

	//disabled until it works
	/*
	@Test
	void User_updateTest(){
		StringBuilder sb = new StringBuilder();
		String username = sb.append("Test user to update -- ").append(metadata).toString();

		User user = new User();
		user._metadata = metadata;
		user.setUsername(username);
		user.setPassword("pass");
		user.setFirstname("This is before updating.");
		System.out.println(user.toString());
		userDB.insertUser(user);

		User toUpdate = userDB.findUser(username);
		userDB.deleteUser(toUpdate);
		assertNull(userDB.findUser(username));

		toUpdate.setFirstname("This is after updating.");
		System.out.println(toUpdate.toString());
		userDB.updateUser(user);

		User updated = userDB.findUser(username);
		System.out.println(updated.toString());

		assertEquals("This is after updating.", updated.getFirstname());
	} */

	@Test
	void User_Authenticate_test(){
		String password = "Password!";

		User user = new User();
		user._metadata = metadata;
		user.setUsername(new StringBuilder().append("auth -- ").append(metadata).toString());
		user.setPassword(password);
		userDB.insertUser(user);

		//make sure the wrong username results in failure
		String wrong = new StringBuilder().append("wrong -- ").append(metadata).toString();
		assertEquals(userDB.authenticate(wrong, password), UserDB.UserAuthenticationStatus.FAILED);

		//make sure right username with wrong password results in failure
		assertEquals(userDB.authenticate(user.getUsername(), "wrong"), UserDB.UserAuthenticationStatus.FAILED);

		//make sure correct username+pw results in success
		assertEquals(userDB.authenticate(user.getUsername(), password), UserDB.UserAuthenticationStatus.SUCCESSFUL);
	}


	@Test
	void zzzzz_cleanup(){
		userDB.deleteByMetadata(metadata);
	}


}
