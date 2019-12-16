package com.org.simplelab.resttests;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.restcontrollers.CourseRESTController;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.DBTestUtils;
import com.org.simplelab.utils.JSONBuilder;
import com.org.simplelab.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

import static com.org.simplelab.restrequest.RESTRequest.RequestType.DELETE;
import static com.org.simplelab.restrequest.RESTRequest.RequestType.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseRESTTests extends RESTTestBaseConfig {

    private RESTRequest courseRequest;

    @BeforeEach
    void loadRestRequest() {
        this.courseRequest = new RESTRequest(mvc, CourseRESTController.BASE_MAPPING, false);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddCourse() throws Exception{
        CourseValidator cv = new CourseValidator();
        cv.setName(metadata);
        cv.setDescription(metadata);
        cv.setCourse_id(metadata);
        courseRequest.sendData(POST, "", JSONBuilder.asJson(cv))
                     .andExpectSuccess(true);

        List<Course> found = courseDB.findByCourseId(cv.getCourse_id());
        assertEquals(1, found.size());
        assertEquals(cv.getName(), found.get(0).getName());
        assertEquals(cv.getDescription(), found.get(0).getDescription());
        assertEquals(cv.getDescription(), found.get(0).getCourse_id());

        //test invalid field
        cv.setName(null);
        cv.setCourse_id(metadata);
        courseRequest.sendData(POST, "", JSONBuilder.asJson(cv))
                .andExpectError(CourseValidator.EMPTY_FIELD);

        //test duplicate courseid;
        cv.setName(metadata);
        courseRequest.sendData(POST, "", JSONBuilder.asJson(cv))
                .andExpectError(CourseValidator.DUPLICATE_ID);


    }

    @Test
    @WithMockUser(username = username, password = username)
    void testDelete() throws Exception{
        int numCourses = 5;
        Course[] created = new Course[numCourses];
        CourseValidator[] toDelete = new CourseValidator[numCourses];
        IntStream.range(0, numCourses)
                .forEach(i -> {
                    try {
                        created[i] = TestUtils.createJunkCourse(userDB.findById(user_id));
                        courseDB.insert(created[i]);
                        CourseValidator cv = new CourseValidator();
                        cv.setCourse_id(created[i].getCourse_id());
                        toDelete[i] = cv;
                    } catch (Exception e) {}
                });
        courseRequest.sendData(DELETE, CourseRESTController.DELETE_MAPPING, JSONBuilder.asJson(toDelete))
                .andExpectSuccess(true);
        Arrays.stream(created).forEach( c -> assertEquals(0, courseDB.findByCourseId(c.getCourse_id()).size()));

    }

    @Test
    @WithMockUser(username = username, password = username)
    void testDeleteWithLabs() throws Exception{
        int numLabs = 4;
        Course c = new Course();
        c.setCreator(userDB.findById(user_id));
        c.setCourse_id(metadata);
        c.setName(metadata);
        c.setDescription(metadata);
        c.setInvite_code(metadata);
        courseDB.insert(c);

        List<Lab> labs = new ArrayList<>();
        long[] idsToAdd = new long[numLabs];
        IntStream.range(0, numLabs).forEach( i -> {
            Lab junkLab = TestUtils.createJunkLab();
            labs.add(junkLab);
            try {
                idsToAdd[i] = DBTestUtils.insertAndGetId(junkLab, labDB);
            }  catch (Exception e) {}
        });
        DTO.CourseAddLabsDTO dto = new DTO.CourseAddLabsDTO();
        dto.setLab_ids(idsToAdd);
        dto.setCourse_id(c.getCourse_id());
        courseRequest.sendData(POST, CourseRESTController.ADD_LABS_TO_COURSE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        CourseValidator cv = new CourseValidator();
        cv.setCourse_id(c.getCourse_id());
        courseRequest.sendData(DELETE, CourseRESTController.DELETE_MAPPING, JSONBuilder.asJson(new CourseValidator[]{cv}))
                .andExpectSuccess(true);

        assertEquals(0, courseDB.findByCourseId(c.getCourse_id()).size());

        //make sure labs arent deleted
        labs.forEach( lab -> assertEquals(1, labDB.getRepository().findByName(lab.getName()).size()));
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testDeleteWithLabsAndEquipmentAndSteps() throws Exception{
        Course c = new Course();
        c.setInvite_code(metadata);
        c.setDescription(metadata);
        c.setName(metadata);
        c.setCourse_id(metadata);
        c.setCreator(userDB.findById(user_id));
        courseDB.insert(c);

        int num = 5;
        List<Lab> labs = new ArrayList<>();
        long[] ids = new long[num];
        IntStream.range(0, num).forEach( i -> {
            Lab junkLab = TestUtils.createJunkLabWithSteps(num);
            Set<Equipment> junkEquipment = new HashSet<>();
            IntStream.range(0, num).forEach( j -> {
                junkEquipment.add(TestUtils.createJunkEquipmentWithProperties(2));
            });
            junkLab.setEquipments(junkEquipment);
            labs.add(junkLab);
            try {
                ids[i] = DBTestUtils.insertAndGetId(junkLab, labDB);
            } catch(Exception e) {System.out.println(e.getStackTrace()); }
        });

        DTO.CourseAddLabsDTO dto = new DTO.CourseAddLabsDTO();
        dto.setCourse_id(c.getCourse_id());
        dto.setLab_ids(ids);
        courseRequest.sendData(POST, CourseRESTController.ADD_LABS_TO_COURSE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        CourseValidator cv = new CourseValidator();
        cv.setCourse_id(c.getCourse_id());
        courseRequest.sendData(DELETE, CourseRESTController.DELETE_MAPPING, JSONBuilder.asJson(new CourseValidator[]{cv}))
                .andExpectSuccess(true);

        //make sure labs are still there
        labs.forEach(lab -> {
            List<Lab> foundLabs = labDB.getRepository().findByName(lab.getName());
            assertEquals(1, foundLabs.size());
            Lab referenceLab = foundLabs.get(0);
            assertEquals(5, referenceLab.getEquipments().size());
            assertEquals(lab.getEquipments(), referenceLab.getEquipments());
            //make sure properties are still there
            assertEquals(2, referenceLab.getEquipments().iterator().next().getProperties().size());
            //make sure steps are still there
            assertEquals(5, referenceLab.getSteps().size());
        });
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddLabs() throws Exception{
        Course c = new Course();
        c.setCourse_id(metadata);
        c.setName(metadata);
        c.setInvite_code(metadata);
        c.setDescription(metadata);
        c.setCreator(userDB.findById(user_id));
        courseDB.insert(c);

        //test adding 1 lab to course
        Lab junkLab = TestUtils.createJunkLab();
        long toAdd = DBTestUtils.insertAndGetId(junkLab, labDB);
        DTO.CourseAddLabsDTO dto = new DTO.CourseAddLabsDTO();
        dto.setCourse_id(c.getCourse_id());
        dto.setLab_ids(new long[]{toAdd});
        courseRequest.sendData(POST, CourseRESTController.ADD_LABS_TO_COURSE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        Set<Lab> found = courseDB.getLabsOfCourse(c.getCourse_id());
        assertEquals(1, found.size());
        assertEquals(junkLab.getName(), found.iterator().next().getName());

        //add another lab to the course
        Lab junkLabTwo = TestUtils.createJunkLab();
        long toAddTwo = DBTestUtils.insertAndGetId(junkLabTwo, labDB);
        dto.setLab_ids(new long[]{toAddTwo});
        courseRequest.sendData(POST, CourseRESTController.ADD_LABS_TO_COURSE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        found = courseDB.getLabsOfCourse(c.getCourse_id());
        assertEquals(2, found.size());
        assertTrue(found.contains(junkLab));
        assertTrue(found.contains(junkLabTwo));

        //test foreign key -- add the same labs to different course
        String newMetadata = metadata+"NEW";
        Course foreignkey = new Course();
        foreignkey.setName(newMetadata);
        foreignkey.setDescription(newMetadata);
        foreignkey.setInvite_code(newMetadata);
        foreignkey.setCourse_id(newMetadata);
        foreignkey.setCreator(userDB.findById(user_id));
        courseDB.insert(foreignkey);

        DTO.CourseAddLabsDTO newDto = new DTO.CourseAddLabsDTO();
        newDto.setCourse_id(foreignkey.getCourse_id());
        newDto.setLab_ids(new long[]{toAdd, toAddTwo});
        courseRequest.sendData(POST, CourseRESTController.ADD_LABS_TO_COURSE_MAPPING, JSONBuilder.asJson(newDto))
                .andExpectSuccess(true);
        found = courseDB.getLabsOfCourse(foreignkey.getCourse_id());
        assertEquals(2, found.size());
        assertTrue(found.contains(junkLab));
        assertTrue(found.contains(junkLabTwo));

    }

    @Test
    @Transactional
    @WithMockUser(username = username, password = username)
    void testAddStudent() throws Exception{
        User student1 = TestUtils.createJunkUser();
        User student2 = TestUtils.createJunkUser();
        Course c = TestUtils.createJunkCourse();
        c.setCreator(userDB.findById(user_id));
        courseDB.insert(c);
        userDB.insert(student1);
        userDB.insert(student2);

        DTO.CourseUpdateStudentListDTO dto = new DTO.CourseUpdateStudentListDTO();
        dto.setCourse_id(c.getCourse_id());
        dto.setUsernameList(Arrays.asList(new String[]{student1.getUsername()}));

        courseRequest.sendData(POST, CourseRESTController.ADD_STUDENT_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        List<User> foundStudents = courseDB.getStudentsOfCourse(c.getCourse_id());
        assertEquals(1, foundStudents.size());
        assertEquals(student1.getUsername(), foundStudents.iterator().next().getUsername());

        //add second student
        dto.setUsernameList(Arrays.asList(new String[]{student2.getUsername()}));
        courseRequest.sendData(POST, CourseRESTController.ADD_STUDENT_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);
        foundStudents = courseDB.getStudentsOfCourse(c.getCourse_id());
        //foundStudents = found.get(0).getStudents();
        assertEquals(2, foundStudents.size());
        foundStudents.forEach( student -> {
            assertTrue(student.getUsername().equals(student1.getUsername())
                                || student.getUsername().equals(student2.getUsername()));
        });

        //test adding students to another course for foreign key
        Course second_c = TestUtils.createJunkCourse();
        second_c.setCreator(userDB.findById(user_id));
        courseDB.insert(second_c);

        dto.setCourse_id(second_c.getCourse_id());
        dto.setUsernameList(Arrays.asList(new String[]{student1.getUsername(), student2.getUsername()}));
        courseRequest.sendData(POST, CourseRESTController.ADD_STUDENT_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        foundStudents = courseDB.getStudentsOfCourse(c.getCourse_id());
        assertEquals(2, foundStudents.size());
        foundStudents.forEach( student -> {
            assertTrue(student.getUsername().equals(student1.getUsername())
                    || student.getUsername().equals(student2.getUsername()));
        });
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testDeleteStudent() throws Exception{
        Course c = TestUtils.createJunkCourse();
        User user1 = TestUtils.createJunkUser();
        User user2 = TestUtils.createJunkUser();
        c.getStudents().addAll(Arrays.asList(new User[]{user1, user2}));
        courseDB.insert(c);

        DTO.CourseUpdateStudentListDTO dto = new DTO.CourseUpdateStudentListDTO();
        dto.setCourse_id(c.getCourse_id());
        dto.setUsernameList(Arrays.asList(new String[]{user1.getUsername(), user2.getUsername()}));

        courseRequest.sendData(DELETE, CourseRESTController.DELETE_STUDENTS_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        courseRequest.sendData(POST, CourseRESTController.GET_STUDENTS_MAPPING, JSONBuilder.asJson(dto))
                .andExpectData("[]");

    }

}
