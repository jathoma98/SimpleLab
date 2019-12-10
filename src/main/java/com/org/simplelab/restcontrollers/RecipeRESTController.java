package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.services.RecipeDB;
import com.org.simplelab.database.validators.RecipeValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if(recipeDB.isRecipeExist(dto.getEquipmentOne(), dto.getEquipmentTwo()))return RRO.sendErrorMessage("Recipe Exist");
        RecipeValidator rv = new RecipeValidator();
        rv.setEquipmentOne(equipmentDB.findById(dto.getEquipmentOne()));
        rv.setEquipmentTwo(equipmentDB.findById(dto.getEquipmentTwo()));
        rv.setResult(equipmentDB.findById(dto.getResult()));
        rv.setRatioOne(dto.getRatioOne());
        rv.setRatioTwo(dto.getRatioTwo());
        return super.addEntity(rv);
    }

    @GetMapping(LOAD_RECIPES)
    public RRO<List<Recipe>> loadRecipe (){
        long user_id = getUserIdFromSession();
        RRO rro = new RRO<List<Recipe>>();
        List<Recipe> recipes = recipeDB.getRecipeByCreateId(user_id);
        if (recipes == null || recipes.size() == 0) {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setData(recipes);
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }


}
