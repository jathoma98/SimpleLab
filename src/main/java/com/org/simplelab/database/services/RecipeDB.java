package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.repositories.sql.RecipeRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Component
@Transactional
public class RecipeDB extends DBService<Recipe> {

    @Autowired
    private RecipeRepository repository;


}
