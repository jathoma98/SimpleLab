package com.org.simplelab.database.services.restservice;

import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.repositories.sql.RecipeRepository;
import com.org.simplelab.database.services.SQLService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Component
@Transactional
public class RecipeDB extends SQLService<Recipe> {

    @Autowired
    private RecipeRepository repository;


    public List<Recipe> getRecipeByCreateId(long id){
        return repository.findByCreator_id(id);
    }

    public boolean isRecipeExist(long a, long b, long c){
        List<Recipe> rs = repository.findByEquipment_one_idAndEquipment_two_idAndResult(a, b, c);
        return rs.size() == 0 ? false : true;
    }

    public boolean deleteById(long id){
        return super.deleteById(id);
    }
}
