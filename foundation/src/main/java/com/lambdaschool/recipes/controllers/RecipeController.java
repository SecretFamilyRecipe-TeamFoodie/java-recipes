package com.lambdaschool.recipes.controllers;

import com.lambdaschool.recipes.models.Recipe;
import com.lambdaschool.recipes.models.RecipeMinimal;
import com.lambdaschool.recipes.models.User;
import com.lambdaschool.recipes.repository.UserRepository;
import com.lambdaschool.recipes.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @Autowired
    UserRepository userRepository;

    //    `GET recipes/all`
//    Returns all recipes that the user is authorized to see. For admins, this is all recipes. For users, this is recipes where they are the owner or guest.
    @GetMapping(value = "/all",
            produces = "application/json")
    public ResponseEntity<?> listAuthedRecipes()
    {
        // Todo: find all if logged in as Admin

        User current = userRepository.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());

        List<RecipeMinimal> result = recipeService.findMinimalsByOwner(current);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // `GET recipes/{id}
    // returns a single recipe
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getRecipeById(@PathVariable long id)
    {
        RecipeMinimal result = recipeService.findMinimalById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //    `POST recipes/new`
//    Creates a new recipe.
    @PostMapping(value = "/new", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> postNewRecipe(@RequestBody RecipeMinimal minimal)
    {
        Recipe result = recipeService.saveFromMinimal(minimal);
        return new ResponseEntity<>(new RecipeMinimal(result), HttpStatus.CREATED);
    }

    //    `PUT recipes/{id}`
//    Updates the recipe with id {id}.
    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> putRecipe(@RequestBody RecipeMinimal minimal, @PathVariable long id)
    {
        minimal.setRecipeid(id);
        Recipe result = recipeService.saveFromMinimal(minimal);
        return new ResponseEntity<>(new RecipeMinimal(result), HttpStatus.OK);
    }

    //    `PATCH recipes/{id}`
//    Updates the recipe with id {id}.
    @PatchMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> patchRecipe(@RequestBody RecipeMinimal minimal, @PathVariable long id)
    {
        Recipe result = recipeService.updateFromMinimal(minimal, id);
        return new ResponseEntity<>(new RecipeMinimal(result), HttpStatus.OK);
    }

    //    `DELETE recipes/{id}`
//    Deletes the recipe with id {id}.
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable long id)
    {
        recipeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}