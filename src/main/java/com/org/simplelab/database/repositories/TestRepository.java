package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.CourseSQL;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<CourseSQL, Long> {

}
