package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.database.repositories.sql.StepRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Component
@Transactional
public class StepDB extends DBService<Step>{

    @Autowired
    private StepRepository repository;

    public boolean insert(Step step) throws EntityDBModificationException{
        return super.insert(step);
    };

    public List<Step> findByLabId(long id){
        return repository.findByLab_id(id);
    }
}
