package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.services.RecipeDB;
import com.org.simplelab.database.validators.RecipeValidator;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RecipeRESTController.BASE_MAPPING)
@Getter
public class RecipeRESTController extends BaseRESTController<Recipe> {

    public static final String BASE_MAPPING = "/recipe/rest";
    public static final String RECIPE_ID_MAPPING = BASE_MAPPING + "/{recipe_id}";

    @Autowired
    private RecipeDB db;

    @PostMapping
    @Transactional
    //TODO: make sure you define the validator
    public RRO addRecipe(RecipeValidator rv){
        return super.addEntity(rv);
    }



}
