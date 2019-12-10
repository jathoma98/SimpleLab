package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Recipe;
import lombok.Data;

@Data
public class RecipeValidator extends Validator<Recipe> {
    Equipment equipmentOne;
    Equipment equipmentTwo;
    Equipment result;
    double ratioOne;
    double ratioTwo;

    @Override
    public void validate() throws InvalidFieldException {
        //dont need anything here for testing
        //TODO: validate stuff later
    }

    @Override
    public Recipe build() {
        Recipe recipe = new Recipe();
        recipe.setEquipmentOne(equipmentOne);
        recipe.setEquipmentTwo(equipmentTwo);
        recipe.setResult(result);
        recipe.setRatioOne(this.ratioOne);
        recipe.setRatioTwo(this.ratioTwo);
        return recipe;
    }
}
