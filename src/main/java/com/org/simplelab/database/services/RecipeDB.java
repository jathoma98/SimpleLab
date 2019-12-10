package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.repositories.sql.RecipeRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Component
@Transactional
public class RecipeDB extends DBService<Recipe> {

    @Autowired
    private RecipeRepository repository;

    public boolean insert(Recipe recipe) throws EntityDBModificationException{
        return super.insert(recipe);
    };

    public List<Recipe> getRecipeByCreateId(long id){
        return repository.findByCreator_id(id);
    }

    public boolean isRecipeExist(long a, long b){
        List<Recipe> rs = repository.findByEquipment_one_idAndEquipment_two_id(a, b);
        return rs.size() == 0 ? false : true;
    }

}
