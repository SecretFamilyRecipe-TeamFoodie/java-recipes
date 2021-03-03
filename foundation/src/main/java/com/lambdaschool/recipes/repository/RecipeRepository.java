package com.lambdaschool.recipes.repository;

import com.lambdaschool.recipes.models.Recipe;
import org.springframework.data.repository.CrudRepository;

/**
 * The CRUD Repository connecting Recipes to the rest of the application
 */
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Recipe findRecipeByName(String name);

    Recipe addRecipe();

    void updateRecipe();
}
