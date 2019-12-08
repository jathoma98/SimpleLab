package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.Recipe;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RecipeRepository extends BaseRepository<Recipe> {

}
