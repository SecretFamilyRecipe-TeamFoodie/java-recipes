package com.lambdaschool.recipes.repository;

import com.lambdaschool.recipes.models.Recipe;
import com.lambdaschool.recipes.models.User;
import com.lambdaschool.recipes.models.UserRecipe;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    Recipe findByName(String name);

    List<Recipe> findAllByName(String name);

    List<Recipe> findAllByOwner(@NotNull User owner);

    List<Recipe> findAllByGuestsContains(@NotNull UserRecipe guest);
}