package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.services.RecipeDB;
import com.org.simplelab.database.validators.RecipeValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RecipeRESTController.BASE_MAPPING)
@Getter
public class RecipeRESTController extends BaseRESTController<Recipe> {

    public static final String BASE_MAPPING = "/recipe/rest";
    public static final String RECIPE_ID_MAPPING = "/{recipe_id}";
    public static final String LOAD_RECIPES = "/loadRecipe";

    @Autowired
    private RecipeDB db;

    @PostMapping
    public RRO<String> addRecipe(@RequestBody DTO.AddRecipeDTO dto){
        RecipeValidator rv = new RecipeValidator();
        rv.setEquipmentOne(equipmentDB.findById(dto.getEquipmentOne()));
        rv.setEquipmentTwo(equipmentDB.findById(dto.getEquipmentTwo()));
        rv.setResult(equipmentDB.findById(dto.getResult()));
        rv.setRatioOne(dto.getRatioOne());
        rv.setRatioTwo(dto.getRatioTwo());
        return super.addEntity(rv);
    }




}
