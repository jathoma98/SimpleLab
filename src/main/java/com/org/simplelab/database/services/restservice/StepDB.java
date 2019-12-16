package com.org.simplelab.database.services.restservice;

import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.database.repositories.sql.StepRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.exception.EntityDBModificationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Component
@Transactional
public class StepDB extends SQLService<Step> {

    @Autowired
    private StepRepository repository;


    public boolean delete(long id) throws EntityDBModificationException {
        return super.deleteById(id);
    };

    public boolean update(Step step) throws EntityDBModificationException{
        return super.update(step);
    };
    public List<Step> findByLabId(long id){
        return repository.findByLab_id(id);
    }
}
