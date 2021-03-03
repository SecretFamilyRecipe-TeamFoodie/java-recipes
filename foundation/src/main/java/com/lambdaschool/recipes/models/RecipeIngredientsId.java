package com.lambdaschool.recipes.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RecipeIngredientsId implements Serializable {
    private long recipe;

    private long ingredient;

    public RecipeIngredientsId() {
    }

    public long getRecipe() {
        return recipe;
    }

    public void setRecipe(long recipe) {
        this.recipe = recipe;
    }

    public long getIngredient() {
        return ingredient;
    }

    public void setIngredient(long ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RecipeIngredientsId that = (RecipeIngredientsId) obj;
        return recipe == that.recipe &&
                ingredient == that.ingredient;
    }
}

