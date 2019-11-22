package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Course;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<Course, Long> {

}
