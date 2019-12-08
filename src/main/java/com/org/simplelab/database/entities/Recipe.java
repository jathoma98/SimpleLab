package com.org.simplelab.database.entities;

import com.org.simplelab.controllers.BaseController;
import lombok.Data;

@Data
public class Recipe extends BaseTable {
    //Recipe which represents that no recipe was found. Replaces "null" return
    public static final Recipe NO_RECIPE = NO_RECIPE_GEN();

    private String name;

    /*
    * Equipment_1
    *
    * Equipment_2
    *
    * Result_Equipment
    *
    *
    * */




    public boolean exists(){
        return getId() != -1;
    }

    private static Recipe NO_RECIPE_GEN(){
        Recipe r = new Recipe();
        r.setName("NO RECIPE");
        r.setId(-1);
        return r;
    }


}
