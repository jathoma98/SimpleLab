package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.sql.Recipe;
import lombok.Data;

@Data
public class RecipeValidator extends Validator<Recipe> {

    @Override
    public void validate() throws InvalidFieldException {
        //dont need anything here for testing
        //TODO: validate stuff later
    }

    @Override
    public Recipe build() {
        //modelmapper can be buggy sometimes, just manually assign fields for now
        //and maybe use modelmapper later.
        return null;
    }
}
