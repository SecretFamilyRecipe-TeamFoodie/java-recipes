package com.lambdaschool.recipes.services;

import com.lambdaschool.recipes.models.Recipe;
import com.lambdaschool.recipes.models.RecipeMinimal;
import com.lambdaschool.recipes.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    Recipe findById(Long id);
    Recipe findByName(String name);
    List<Recipe> findByOwner(User user);
    List<Recipe> findByOwnerOrGuest(User user);

    List<RecipeMinimal> findAllMinimals();
    RecipeMinimal findMinimalById(Long id);
    RecipeMinimal findMinimalByName(String name);
    List<RecipeMinimal> findMinimalsByOwner(User user);
    List<RecipeMinimal> findMinimalsByOwnerOrGuest(User user);

    Recipe save(Recipe recipe);
    Recipe saveFromMinimal(RecipeMinimal minimal);

    void saveDirect(Recipe recipe);

    Recipe update(Recipe recipe, Long id);
    Recipe updateFromMinimal(RecipeMinimal minimal, Long id);

    void deleteById(Long id);
    void deleteAll();
}