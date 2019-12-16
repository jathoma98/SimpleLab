package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.services.restservice.RecipeDB;
import com.org.simplelab.database.validators.RecipeValidator;
import com.org.simplelab.exception.InvalidFieldException;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RecipeRESTController.BASE_MAPPING)
@Getter
public class RecipeRESTController extends BaseRESTController<Recipe> {

    public static final String BASE_MAPPING = "/recipe/rest";
    public static final String RECIPE_ID_MAPPING = "/{recipe_id}";
    public static final String LOAD_RECIPES = "/loadRecipe";
    public static final String LOAD_RECIPES_BY_OWN_ID = LOAD_RECIPES + "/{create_id}";


    @Autowired
    private RecipeDB db;

    @PostMapping
    public RRO<String> addRecipe(@RequestBody DTO.AddRecipeDTO dto)  {
        if(recipeDB.isRecipeExist(dto.getEquipmentOne(), dto.getEquipmentTwo(), dto.getResult()))return RRO.sendErrorMessage("Recipe Exist");
        RecipeValidator rv = new RecipeValidator();
        rv.setEquipmentOne(equipmentDB.findById(dto.getEquipmentOne()));
        rv.setEquipmentTwo(equipmentDB.findById(dto.getEquipmentTwo()));
        rv.setResult(equipmentDB.findById(dto.getResult()));
        rv.setRatioOne(dto.getRatioOne());
        rv.setRatioTwo(dto.getRatioTwo());
        rv.setRatioThree(dto.getRatioThree());
        try{
            rv.validate();
        }catch (InvalidFieldException e) {
            return RRO.sendErrorMessage(e.getMessage());
        }
        return super.addEntity(rv);
    }

    @GetMapping(LOAD_RECIPES)
    public RRO<List<Recipe>> loadRecipe (){
        long user_id = getUserIdFromSession();
        RRO rro = new RRO<List<Recipe>>();
        List<Recipe> recipes = recipeDB.getRecipeByCreateId(user_id);
        rro.setData(recipes);
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    @GetMapping(LOAD_RECIPES_BY_OWN_ID)
    public RRO<List<Recipe>> loadRecipeByCreator_id (@PathVariable("create_id") Long own_id){
        RRO rro = new RRO<List<Recipe>>();
        List<Recipe> recipes = recipeDB.getRecipeByCreateId(own_id);
        rro.setData(recipes);
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        return rro;
    }

    @DeleteMapping(RECIPE_ID_MAPPING)
    public RRO<String> deleteRecipe(@PathVariable Long recipe_id){
        RRO<String> rro = new RRO();
        recipeDB.deleteById(recipe_id);
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    @PatchMapping(RECIPE_ID_MAPPING)
    public RRO<String> updateRecipe(@PathVariable Long recipe_id, @RequestBody DTO.AddRecipeDTO dto){
        Recipe recipe = recipeDB.findById(recipe_id);
        Equipment equipmentOne = dto.getEquipmentOne() == recipe.getEquipmentOne().getId() ?
                recipe.getEquipmentOne():equipmentDB.findById(dto.getEquipmentOne());
        Equipment equipmentTwo = dto.getEquipmentTwo() == recipe.getEquipmentTwo().getId() ?
                recipe.getEquipmentTwo():equipmentDB.findById(dto.getEquipmentTwo());
        Equipment result = dto.getResult() == recipe.getResult().getId() ?
                recipe.getResult():equipmentDB.findById(dto.getResult());
        recipe.setRatioOne(dto.getRatioOne());
        recipe.setRatioTwo(dto.getRatioTwo());
        return null;
    }
}
