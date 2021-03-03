package com.lambdaschool.recipes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "recipeingredients")
@IdClass(RecipeIngredientsId.class)
public class RecipeIngredients
        extends Auditable
        implements Serializable {

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "recipeid")
    @JsonIgnoreProperties(value = "ingredients", allowSetters = true)
    private Recipe recipe;

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "ingredientid")
    @JsonIgnoreProperties(value = "recipes", allowSetters = true)
    private Ingredient ingredient;

    public RecipeIngredients() {
    }

    public RecipeIngredients(@NotNull Recipe recipe, @NotNull Ingredient ingredient) {
        this.recipe = recipe;
        this.ingredient = ingredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RecipeIngredients)) {
            return false;
        }
        RecipeIngredients that = (RecipeIngredients) obj;
        return ((recipe == null) ? 0 : recipe.getRecipeid()) ==
                ((that.recipe == null) ? 0 : that.recipe.getRecipeid()) &&
                ((ingredient == null) ? 0 : ingredient.getIngredientid()) ==
                        ((that.ingredient == null) ? 0 : that.ingredient.getIngredientid());
    }

    @Override
    public int hashCode() {
        return 37;
    }
}
