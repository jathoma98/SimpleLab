package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.exception.InvalidFieldException;
import lombok.Data;

@Data
public class RecipeValidator extends Validator<Recipe> {
    Equipment equipmentOne;
    Equipment equipmentTwo;
    Equipment result;
    double ratioOne;
    double ratioTwo;
    double ratioThree;

    @Override
    public void validate() throws InvalidFieldException {
        StringBuilder sb = new StringBuilder();
        if (equipmentOne == null) {
            sb.append("invalid equipment one\n");
        }
        if (equipmentTwo == null) {
            sb.append("invalid equipment two\n");
        }
        if (result == null) {
            sb.append("invalid equipment result\n");
        }
        if (ratioOne <= 0){
            sb.append("ratioOne is equal to or less than 0\n");
        }
        if (ratioTwo <= 0){
            sb.append("ratioTwo is equal to or less than 0\n");
        }
        if (ratioThree <= 0){
            sb.append("ratioThree is equal to or less than 0\n");
        }
        if (sb.length() > 0)
            throw new InvalidFieldException(sb.toString());
    }

    @Override
    public Recipe build() {
        Recipe recipe = new Recipe();
        recipe.setEquipmentOne(equipmentOne);
        recipe.setEquipmentTwo(equipmentTwo);
        recipe.setResult(result);
        recipe.setRatioOne(this.ratioOne);
        recipe.setRatioTwo(this.ratioTwo);
        recipe.setRatioThree(this.ratioThree);
        equipmentOne.getEquipmentOne().add(recipe);
        equipmentTwo.getEquipmentTwo().add(recipe);
        return recipe;
    }
}
