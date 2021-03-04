package com.lambdaschool.recipes.services;

import com.lambdaschool.recipes.models.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> findAll();

    Ingredient findByName(String name);

    Ingredient findById(long id);

    void deleteAll();
}
