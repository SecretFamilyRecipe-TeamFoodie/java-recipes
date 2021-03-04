package com.lambdaschool.recipes.models;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RecipeMinimal {
    private long recipeid;

    @NotNull
    private String name;

    private String source;

    private String instructions;

    private String category;

    private List<String> ingredients = new ArrayList<>();

    private List<String> guests = new ArrayList<>();

    public RecipeMinimal() {
    }

    public RecipeMinimal(Recipe full) {
        recipeid = full.getRecipeid();
        name = full.getName();
        source = full.getSource();
        category = full.getCategory();
        instructions = full.getInstructions();

        for(RecipeIngredient ri : full.getIngredients())
        {
            ingredients.add(ri.getIngredient().getName());
        }

        for(UserRecipe ur : full.getGuests())
        {
            guests.add(ur.getUser().getUsername());
        }
    }

    public long getRecipeid() {
        return recipeid;
    }

    public void setRecipeid(long recipeId) {
        this.recipeid = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }
}
