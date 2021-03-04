package com.lambdaschool.recipes.repository;

import com.lambdaschool.recipes.models.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    Ingredient findByName(String name);
}
