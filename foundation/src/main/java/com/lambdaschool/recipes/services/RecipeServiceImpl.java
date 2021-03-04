package com.lambdaschool.recipes.services;

import com.lambdaschool.recipes.exceptions.ResourceNotFoundException;
import com.lambdaschool.recipes.models.*;
import com.lambdaschool.recipes.repository.IngredientRepository;
import com.lambdaschool.recipes.repository.RecipeRepository;
import com.lambdaschool.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "recipeService")
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<Recipe> findAll() {
        List<Recipe> result = new ArrayList<>();
        recipeRepository.findAll().iterator().forEachRemaining(result::add);
        return result;
    }

    @Override
    public List<RecipeMinimal> findAllMinimals() {
        List<Recipe> fulls = findAll();
        List<RecipeMinimal> result = new ArrayList<>();
        for(Recipe r : fulls)
        {
            result.add(new RecipeMinimal(r));
        }
        return result;
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find recipe with id " + id + "."));
    }

    @Override
    public RecipeMinimal findMinimalById(Long id){
        return new RecipeMinimal(findById(id));
    }

    @Override
    public Recipe findByName(String name) {
        return recipeRepository.findByName(name);
    }

    @Override
    public RecipeMinimal findMinimalByName(String name)
    {
        return new RecipeMinimal(findByName(name));
    }

    @Override
    public List<Recipe> findByOwner(User user) {
        return recipeRepository.findAllByOwner(user);
    }

    @Override
    public List<RecipeMinimal> findMinimalsByOwner(User user) {
        List<Recipe> fulls = findByOwner(user);
        List<RecipeMinimal> result = new ArrayList<>();
        for(Recipe r : fulls)
        {
            result.add(new RecipeMinimal(r));
        }
        return result;
    }

    @Override
    public List<Recipe> findByOwnerOrGuest(User user) {
        return null; // todo:  NYI - figure out the @Query for "all recipes connected to a recipeUser connected to this user"
        //return recipeRepository.findAllByOwner(user).addAll(recipeRepository.findAllByGuestsContains(new UserRecipe()));
    }

    @Override
    public List<RecipeMinimal> findMinimalsByOwnerOrGuest(User user) {
        List<Recipe> fulls = findByOwnerOrGuest(user);
        List<RecipeMinimal> result = new ArrayList<>();
        for(Recipe r : fulls)
        {
            result.add(new RecipeMinimal(r));
        }
        return result;
    }

    @Override
    public Recipe save(Recipe recipe) {
        Recipe newRecipe = new Recipe();

        User current = userRepository.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());

        if(recipe.getRecipeid() != 0)
        {
            Recipe oldRecipe = recipeRepository.findById(recipe.getRecipeid()).orElseThrow(() -> new ResourceNotFoundException("Could not find recipe with id " + recipe.getRecipeid() + "."));

            if(!current.getUsername().equalsIgnoreCase(oldRecipe.getOwner().getUsername()))
            {
                throw new AccessDeniedException("You cannot modify another user's recipe.");
            }

            newRecipe.setRecipeid(oldRecipe.getRecipeid());
        }

        newRecipe.setOwner(current);

        newRecipe.setName(recipe.getName());
        newRecipe.setCategory(recipe.getCategory());
        newRecipe.setSource(recipe.getSource());
        newRecipe.setInstructions(recipe.getInstructions());

        for(RecipeIngredient ri : recipe.getIngredients())
        {
            Ingredient ing = ingredientRepository.findByName(ri.getIngredient().getName());
            if(ing == null)
            {
                ing = ingredientRepository.save(new Ingredient(ri.getIngredient().getName()));
            }
            newRecipe.getIngredients().add(new RecipeIngredient(newRecipe, ing));
        }

        for(UserRecipe ur : recipe.getGuests())
        {
            User guest = userRepository.findByUsername(ur.getUser().getUsername());
            if(guest == null)
            {
                throw new ResourceNotFoundException("Cannot find user named " + ur.getUser().getUsername() + " as guest.");
            }
            newRecipe.getGuests().add(new UserRecipe(guest, newRecipe));
        }

        return recipeRepository.save(newRecipe);
    }

    @Override
    public Recipe saveFromMinimal(RecipeMinimal minimal) {
        Recipe newRecipe = new Recipe(minimal);

        User current = userRepository.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());

        if(minimal.getRecipeid() != 0)
        {
            Recipe oldRecipe = recipeRepository.findById(minimal.getRecipeid()).orElseThrow(() -> new ResourceNotFoundException("Could not find recipe with id " + minimal.getRecipeid() + "."));

            if(!current.getUsername().equalsIgnoreCase(oldRecipe.getOwner().getUsername()))
            {
                throw new AccessDeniedException("You cannot modify another user's recipe.");
            }

            newRecipe.setRecipeid(oldRecipe.getRecipeid());
        }

        newRecipe.setOwner(current);

        for(String ingredient : minimal.getIngredients())
        {
            Ingredient ing = ingredientRepository.findByName(ingredient);
            if(ing == null)
            {
                ing = ingredientRepository.save(new Ingredient(ingredient));
            }
            newRecipe.getIngredients().add(new RecipeIngredient(newRecipe, ing));
        }

        for(String guestname : minimal.getGuests())
        {
            User guest = userRepository.findByUsername(guestname);
            if(guest == null)
            {
                throw new ResourceNotFoundException("Cannot find user named " + guestname + " as guest.");
            }
            newRecipe.getGuests().add(new UserRecipe(guest, newRecipe));
        }

        return recipeRepository.save(newRecipe);
    }

    @Override
    public void saveDirect(Recipe recipe) {
        Recipe newRecipe = new Recipe();

        newRecipe.setOwner(userRepository.findByUsername(recipe.getOwner().getUsername()));

        newRecipe.setName(recipe.getName());
        newRecipe.setCategory(recipe.getCategory());
        newRecipe.setSource(recipe.getSource());
        newRecipe.setInstructions(recipe.getInstructions());

        for(RecipeIngredient ri : recipe.getIngredients())
        {
            Ingredient ing = ingredientRepository.findByName(ri.getIngredient().getName());
            if(ing == null)
            {
                ing = ingredientRepository.save(new Ingredient(ri.getIngredient().getName()));
            }
            newRecipe.getIngredients().add(new RecipeIngredient(newRecipe, ing));
        }

        for(UserRecipe ur : recipe.getGuests())
        {
            User guest = userRepository.findByUsername(ur.getUser().getUsername());
            if(guest == null)
            {
                throw new ResourceNotFoundException("Cannot find user named " + ur.getUser().getUsername() + " as guest.");
            }
            newRecipe.getGuests().add(new UserRecipe(guest, newRecipe));
        }

        recipeRepository.save(newRecipe);
    }

    @Override
    public Recipe update(Recipe recipe, Long id) {
        Recipe newRecipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find recipe with id " + id + "."));

        if(recipe.getName() != null) newRecipe.setName(recipe.getName());
        if(recipe.getCategory() != null) newRecipe.setCategory(recipe.getCategory());
        if(recipe.getSource() != null) newRecipe.setSource(recipe.getSource());
        if(recipe.getInstructions() != null) newRecipe.setInstructions(recipe.getInstructions());

        if(recipe.getIngredients().size() != 0) {
            newRecipe.getIngredients().clear();
            for (RecipeIngredient ri : recipe.getIngredients()) {
                Ingredient ing = ingredientRepository.findByName(ri.getIngredient().getName());
                if (ing == null) {
                    ing = ingredientRepository.save(new Ingredient(ri.getIngredient().getName()));
                }
                newRecipe.getIngredients().add(new RecipeIngredient(newRecipe, ing));
            }
        }

        if(recipe.getGuests().size() != 0) {
            newRecipe.getGuests().clear();
            for (UserRecipe ur : recipe.getGuests()) {
                User guest = userRepository.findByUsername(ur.getUser().getUsername());
                if (guest == null) {
                    throw new ResourceNotFoundException("Cannot find user named " + ur.getUser().getUsername() + " as guest.");
                }
                newRecipe.getGuests().add(new UserRecipe(guest, newRecipe));
            }
        }

        return recipeRepository.save(newRecipe);
    }

    @Override
    public Recipe updateFromMinimal(RecipeMinimal minimal, Long id) {
        Recipe newRecipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find recipe with id " + id + "."));

        if(minimal.getName() != null) newRecipe.setName(minimal.getName());
        if(minimal.getCategory() != null) newRecipe.setCategory(minimal.getCategory());
        if(minimal.getSource() != null) newRecipe.setSource(minimal.getSource());
        if(minimal.getInstructions() != null) newRecipe.setInstructions(minimal.getInstructions());

        if(minimal.getIngredients().size() != 0) {
            newRecipe.getIngredients().clear();
            for (String ingredient : minimal.getIngredients()) {
                Ingredient ing = ingredientRepository.findByName(ingredient);
                if (ing == null) {
                    ing = ingredientRepository.save(new Ingredient(ingredient));
                }
                newRecipe.getIngredients().add(new RecipeIngredient(newRecipe, ing));
            }
        }

        if(minimal.getGuests().size() != 0) {
            newRecipe.getGuests().clear();
            for (String guestname : minimal.getGuests()) {
                User guest = userRepository.findByUsername(guestname);
                if (guest == null) {
                    throw new ResourceNotFoundException("Cannot find user named " + guestname + " as guest.");
                }
                newRecipe.getGuests().add(new UserRecipe(guest, newRecipe));
            }
        }

        return recipeRepository.save(newRecipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        recipeRepository.deleteAll();
    }
}

