package com.org.simplelab;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.entities.sql.*;
import com.org.simplelab.database.repositories.sql.UserRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.database.services.projections.Projections;
import com.org.simplelab.database.services.restservice.LabDB;
import com.org.simplelab.database.services.restservice.RecipeDB;
import com.org.simplelab.database.services.restservice.UserDB;
import com.org.simplelab.database.validators.LabValidator;
import com.org.simplelab.restcontrollers.CourseRESTController;
import com.org.simplelab.restcontrollers.LabRESTController;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.utils.DBTestUtils;
import com.org.simplelab.utils.TestUtils;
import org.apache.commons.lang3.SerializationUtils;
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

	@Autowired
	UserRepository ur;
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
		assertTrue(userDB.insert(user).getId() > 0);

		//ensure duplicate insertion returns false
		assertThrows(UserDB.UserInsertionException.class, () -> userDB.insert(user));

		//ensure duplicate insertion doesn't insert an additional instance with the same username
		UserRepository repo = ur;
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

		SQLService.EntitySetManager<Lab, Course> manager = courseDB.getLabsOfCourseByCourseId(c.getCourse_id());
		manager.insert(l);
		courseDB.update(manager.getFullEntity());


		DTO.CourseAddLabsDTO dto = new DTO.CourseAddLabsDTO();
		dto.setLab_ids(new long[]{lr.findByName(metadata).get(0).getId()});
		dto.setCourse_id(c.getCourse_id());
		crc.addLabsToCourse(dto);

		SQLService.EntitySetManager setManager = courseDB.getLabsOfCourseByCourseId(c.getCourse_id());
		assertEquals(1, setManager.getEntitySet().size());
		assertEquals(metadata, l.getName());
	}

	@Test
	@Transactional
	@WithMockUser(username = "COURSETEST", password = username)
	void testAddStudentToCourse() throws Exception{
		Course c = new Course();
		c.set_metadata(metadata);
		c.setCourse_id(metadata);
		c.setName(metadata);
		c.setDescription(metadata);
		User creator = TestUtils.createJunkUser();
		creator.setUsername("COURSETEST");
		c.setCreator(creator);
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

		SQLService.EntitySetManager<User, Course> set = courseDB.getStudentsOfCourseByCourseId(metadata);
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
		SQLService.EntitySetManager<User, Course> original = courseDB.getStudentsOfCourseByCourseId(c.getCourse_id());
		assertEquals(3, original.getEntitySet().size());

		DTO.CourseUpdateStudentListDTO dto = new DTO.CourseUpdateStudentListDTO();
		dto.setCourse_id(c.getCourse_id());
		dto.setUsernameList(usernames);
		crc.deleteStudentList(dto);

		SQLService.EntitySetManager<User, Course> newUsers = courseDB.getStudentsOfCourseByCourseId(c.getCourse_id());
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
		RRO<Lab> returnLab = lrc.labGet(lab.getId());
		assertEquals(returnLab.getData().getName(), lab.getName());
		assertEquals(returnLab.getData().getId(), lab.getId());

		/**
		 * @Test test update lab
		 */
		lv.setName(metadata + "UPDATED");
		lrc.labUpdate(returnLab.getData().getId(), lv);
		returnLab = lrc.labGet(returnLab.getData().getId());
		assertEquals(metadata + "UPDATED", returnLab.getData().getName());

		/**
		 * @Test test delete lab
		 */
		long idToDelete = returnLab.getData().getId();
		lrc.labDelete(idToDelete);
		found = lr.findByName(returnLab.getData().getName());
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
	void foreign_key_test() throws Exception{
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

		SQLService.EntitySetManager<User, Course> found = courseDB.getStudentsOfCourseByCourseId(metadata);
		assertEquals(3, found.getEntitySet().size());

		courseDB.deleteById(found.getFullEntity().getId());
	}

	@Test
	void equipmentTest() throws Exception{
		Equipment e = new Equipment();
		e.setName(metadata);
		e.setType(metadata);


		for (int i = 0; i < 5; i++){
			EquipmentProperty ep = new EquipmentProperty();
			ep.setPropertyKey("test " + i);
			ep.setPropertyValue(Integer.toString(i));
			ep.setParentEquipment(e);
			e.getProperties().add(ep);
		}

		equipmentDB.insert(e);
		Iterable<Equipment> found = er.findAll();
		for (Equipment foundEq: found){
			System.out.println(foundEq.toString());
			System.out.println("Properties: " + foundEq.getProperties().toString());
		}
	}

	@Test
	void lab_deleteByIdTest() throws Exception{
		Lab l = TestUtils.createJunkLab();
		labDB.insert(l);

		Lab found = lr.findByName(l.getName()).get(0);
		long idToDelete = found.getId();

		labDB.deleteById(idToDelete);
		assertEquals(0, lr.findByName(l.getName()).size());

		//test LRC delete lab method
		DTO.UserLabsDTO dto = new DTO.UserLabsDTO();

		int numLabs = 5;
		String[] labsToFind = new String[numLabs];
		for (int i = 0; i < numLabs; i++){
			Lab lab = TestUtils.createJunkLab();
			labsToFind[i] = lab.getName();
			labDB.insert(lab);
		}
		long[] idsToDelete = new long[numLabs];
		int count = 0;
		for (String name: labsToFind){
			Lab foundLabName = lr.findByName(name).get(0);
			idsToDelete[count++] = foundLabName.getId();
		}
		System.out.println(idsToDelete.toString());
		dto.setLids(idsToDelete);
		lrc.deleteLab(dto);

		for (String name: labsToFind){
			assertEquals(0, lr.findByName(name).size());
		}

	}

	@Autowired
	RecipeDB rdb;



	@Test
	void mongoDBinitTest(){
		LabInstance li = new LabInstance();
		Lab l = TestUtils.createJunkLabWithSteps(10);
		Set<Equipment> eqlist = new HashSet<>();
		for (int i = 0; i < 5; i++){
			eqlist.add(TestUtils.createJunkEquipmentWithProperties(5));
		}
		l.setEquipments(eqlist);
		byte[] serializedLab = SerializationUtils.serialize(l);

		li.setSerialized_lab(serializedLab);
		//li.set_metadata(metadata);

		lir.save(li);
		lir.findAll().forEach( (found) -> {
			System.out.println(found.toString());
		});
	}

	@Test
	void testEquipComplexProjection() throws Exception{
		long equip_id = DBTestUtils.insertAndGetId(TestUtils.createJunkEquipmentWithProperties(3), equipmentDB);
		Projections.TeacherEditEquipmentInfo info = equipmentDB.findById(equip_id, Projections.TeacherEditEquipmentInfo.class);
		System.out.println(info.getName());
		info.getProperties().stream().forEach( prop -> System.out.print(prop.getPropertyKey()));
	}

	@Test
	void testSerialize() throws Exception{
		Equipment e = TestUtils.createJunkEquipmentWithProperties(3);
		Equipment deserialized = SerializationUtils.roundtrip(e);

		assertEquals(e, deserialized);

	}


	}


