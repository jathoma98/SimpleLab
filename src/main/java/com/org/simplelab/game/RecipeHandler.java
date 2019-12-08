package com.org.simplelab.game;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Recipe;
import org.springframework.stereotype.Component;

/**
 * Class which handles Recipe interactions in labs.
 */
@Component
public class RecipeHandler {

    /**
     * Finds a recipe which matches the 2 given Equipment objects
     * @return - the Recipe which matches the 2 given objects if it exists,
     *           NO_RECIPE otherwise
     */
    public Recipe findRecipe(Equipment obj1, Equipment obj2){
        return Recipe.NO_RECIPE;
    }

}
