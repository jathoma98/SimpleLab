package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<TestEntity, Integer> {

}
