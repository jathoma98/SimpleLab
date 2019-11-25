package com.org.simplelab;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.repositories.UserRepository;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.services.LabDB;
import com.org.simplelab.database.services.UserDB;
import com.org.simplelab.database.validators.LabValidator;
import com.org.simplelab.restcontrollers.CourseRESTController;
import com.org.simplelab.restcontrollers.LabRESTController;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
class DBTests extends SpringTestConfig {

	static String prefix = "test user -- ";

	@Test
	void aaaaa_contextLoads() {
	}

	@Test
	void User_insertionTest() throws Exception{

		User user = new User();
		StringBuilder sb = new StringBuilder();
		sb.append(prefix).append(metadata);
		String username = sb.toString();

		System.out.println("Inserting user: " + username);
		user.setUsername(username);
		user.setPassword("a password to be hashed");

		//adding metadata to delete this entity later.
		user._metadata = metadata;
		assertEquals(userDB.insert(user), true);

		//ensure duplicate insertion returns false
		assertThrows(UserDB.UserInsertionException.class, () -> userDB.insert(user));

		//ensure duplicate insertion doesn't insert an additional instance with the same username
		UserRepository repo = userDB.DEBUG_getInterface();
		assertEquals(repo.findByUsername(user.getUsername()).size(), 1);

	}

	@Test
	void User_retrievalTest() throws Exception{
		StringBuilder sb = new StringBuilder();
		String username = sb.append("Another test user -- ").append(metadata).toString();

		System.out.println("Retrieving user: " + username);
		User user = new User();
		user.setUsername(username);
		user.setPassword("passy pass");
		user._metadata = metadata;

		userDB.insert(user);
		User found = userDB.findUser(username);
		assertNotNull(found);
		assertEquals(found.getUsername(), username);
	}

	@Test
	void User_deletionTest() throws Exception{
		StringBuilder sb = new StringBuilder();
		String username = sb.append("Test user to delete -- ").append(metadata).toString();

		User user = new User();
		user._metadata = metadata;
		user.setUsername(username);
		user.setPassword("a pass");
		userDB.insert(user);

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
		userDB.insert(user);

		User toUpdate = userDB.findUser(username);
		userDB.deleteUser(toUpdate);
		assertNull(userDB.findUser(username));

		toUpdate.setFirstname("This is after updating.");
		System.out.println(toUpdate.toString());
		userDB.update(user);

		User updated = userDB.findUser(username);
		System.out.println(updated.toString());

		assertEquals("This is after updating.", updated.getFirstname());
	} */

	@Test
	void User_Authenticate_test() throws Exception{
		String password = "Password!";

		User user = new User();
		user._metadata = metadata;
		user.setUsername(new StringBuilder().append("auth -- ").append(metadata).toString());
		user.setPassword(password);
		userDB.insert(user);

		//make sure the wrong username results in failure
		String wrong = new StringBuilder().append("wrong -- ").append(metadata).toString();
		assertEquals(userDB.authenticate(wrong, password), UserDB.UserAuthenticationStatus.FAILED);

		//make sure right username with wrong password results in failure
		assertEquals(userDB.authenticate(user.getUsername(), "wrong"), UserDB.UserAuthenticationStatus.FAILED);

		//make sure correct username+pw results in success
		assertEquals(userDB.authenticate(user.getUsername(), password), UserDB.UserAuthenticationStatus.SUCCESSFUL);
	}

	//weird error that occurred during runtime
	@Test
	void User_authenticate_weird(){
		String breaker1 = "aaaaaaaa";
		String breaker2 = "qqqqqqqq";
		userDB.findUser(breaker2);
		userDB.findUser(breaker1);
	}

	@Autowired
	LabRESTController lrc;

	@Autowired
	LabDB labDB;

	/**
	 * @Test test add equipment to lab endpoint
	 */
	@Test
	@Transactional
	void testEquipmentSetManager(){
		List<Equipment> list = new ArrayList<>();
		for (int i = 0; i < 1; i++){
			Equipment e = new Equipment();
			e.set_metadata(metadata);
			e.setName(metadata);
			list.add(e);
		}
		Lab l = new Lab();
		l.setName(metadata);
		l.set_metadata(metadata);
		lr.save(l);
		l = lr.findByName(metadata).get(0);
		lrc.addEquipmentToLab(l.getId(), list);
		Set<Equipment> found = l.getEquipments();
		assertEquals(1, found.size());
	}

	@Autowired
	CourseRESTController crc;

	/**
	 * @Test add labs to course endpoint.
	 */
	@Test
	@Transactional
	void testAddLabsToCourse() throws Exception{
		Lab l = new Lab();
		l.set_metadata(metadata);
		l.setName(metadata);
		Course c = new Course();
		c.set_metadata(metadata);
		c.setCourse_id(metadata);
		c.setName(metadata);
		c.setDescription(metadata);
		cr.save(c);
		lr.save(l);

		DBService.EntitySetManager<Lab, Course> manager = courseDB.getLabsOfCourseByCourseId(c.getCourse_id());
		manager.insert(l);
		courseDB.update(manager.getFullEntity());


		DTO.CourseAddLabsDTO dto = new DTO.CourseAddLabsDTO();
		dto.setLab_ids(new long[]{lr.findByName(metadata).get(0).getId()});
		dto.setCourse_id(c.getCourse_id());
		crc.addLabsToCourse(dto);

		DBService.EntitySetManager setManager = courseDB.getLabsOfCourseByCourseId(c.getCourse_id());
		assertEquals(1, setManager.getEntitySet().size());
		assertEquals(metadata, l.getName());
	}

	@Test
	@Transactional
	@WithMockUser(username = username, password = username)
	void testAddStudentToCourse() throws Exception{
		Course c = new Course();
		c.set_metadata(metadata);
		c.setCourse_id(metadata);
		c.setName(metadata);
		c.setDescription(metadata);
		cr.save(c);
		List<String> usernames = new ArrayList<>();

		for (int i = 0; i < 3; i++){
			String username = metadata + " " + i;
			usernames.add(username);
			User u = new User();
			u.setUsername(username);
			u.set_metadata(metadata);
			userDB.insert(u);
		}

		DTO.CourseUpdateStudentListDTO dto = new DTO.CourseUpdateStudentListDTO();
		dto.setUsernameList(usernames);
		dto.setCourse_id(metadata);
		crc.addStudentToCourse(dto);

		DBService.EntitySetManager<User, Course> set = courseDB.getStudentsOfCourseByCourseId(metadata);
		assertEquals(3, set.getEntitySet().size());
		assertEquals(metadata, ((User)set.getEntitySet().toArray()[0]).get_metadata());

	}

	@Test
	@Transactional
	void testDeleteFromStudentList(){
		Course c = new Course();
		Set<User> users = new HashSet<>();
		List<String> usernames = new ArrayList<>();
		c.setName(metadata);
		c.setCourse_id(metadata);
		c.set_metadata(metadata);
		for (int i = 0; i < 3; i++){
			User u = new User();
			u.setUsername(metadata + i);
			users.add(u);
			usernames.add(u.getUsername());
		}
		c.setStudents(users);
		cr.save(c);
		DBService.EntitySetManager<User, Course> original = courseDB.getStudentsOfCourseByCourseId(c.getCourse_id());
		assertEquals(3, original.getEntitySet().size());

		DTO.CourseUpdateStudentListDTO dto = new DTO.CourseUpdateStudentListDTO();
		dto.setCourse_id(c.getCourse_id());
		dto.setUsernameList(usernames);
		crc.deleteStudentList(dto);

		DBService.EntitySetManager<User, Course> newUsers = courseDB.getStudentsOfCourseByCourseId(c.getCourse_id());
		assertEquals(0, newUsers.getEntitySet().size());

	}

	@Test
	@Transactional
	@WithMockUser(username = username, password = username)
	void testLabRESTMethods(){
		/**
		 * @Test - test Add Lab
		 */
		LabValidator lv = new LabValidator();
		lv.set_metadata(metadata);
		lv.setName(metadata);
		lv.setDescription(metadata);

		lrc.saveLab(lv);

		List<Lab> found = lr.findByName(metadata);
		assertEquals(1, found.size());
		assertEquals(metadata, found.get(0).getName());
		Lab lab = found.get(0);

		/**
		 * @Test - test Get Lab
		 */
		Lab returnLab = lrc.labGet(lab.getId());
		assertEquals(returnLab.getName(), lab.getName());
		assertEquals(returnLab.getId(), lab.getId());

		/**
		 * @Test test update lab
		 */
		lv.setName(metadata + "UPDATED");
		lrc.labUpdate(returnLab.getId(), lv);
		returnLab = lrc.labGet(returnLab.getId());
		assertEquals(metadata + "UPDATED", returnLab.getName());

		/**
		 * @Test test delete lab
		 */

		long idToDelete = returnLab.getId();
		lrc.labDelete(idToDelete);
		found = lr.findByName(returnLab.getName());
		assertEquals(0, found.size());

	}

	@Test
	void mmtest(){
		ModelMapper mm = DBUtils.getMapper();
		User user = new User();
		user.setUsername(metadata);
		user.setFirstname(metadata);
		long originalTimestamp = user.getTimestamp();
		user.setCreatedDate("ORIGINAL");

		User dto = new User();
		dto.setTimestamp(11111);
		dto.setCreatedDate("REPLACE");
		dto.setLastname("NEW");

		mm.map(dto, user);
		assertEquals(originalTimestamp, user.getTimestamp());
		assertEquals(metadata, user.getUsername());
		assertEquals(metadata, user.getFirstname());
		assertEquals("NEW", user.getLastname());

	}

	@Test
	void foreign_key_test(){
		List<User> students = new ArrayList<>();
		for (int i = 0; i < 3; i++){
			User u = new User();
			u.setUsername(metadata + "STUDENT " + i);
			students.add(u);
		}
		Course c = new Course();
		c.setName(metadata);
		c.setDescription(metadata);
		c.setCourse_id(metadata);
		c.set_metadata(metadata);
		c.setStudents(new HashSet<User>(students));
		courseDB.insert(c);

		DBService.EntitySetManager<User, Course> found = courseDB.getStudentsOfCourseByCourseId(metadata);
		assertEquals(3, found.getEntitySet().size());

		courseDB.deleteById(found.getFullEntity().getId());
	}




}
