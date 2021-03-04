package com.lambdaschool.recipes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredients")
public class Ingredient extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ingredientid;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "ingredient", allowSetters = true)
    private List<RecipeIngredient> recipes = new ArrayList<>();

    public Ingredient() {
    }

    public Ingredient(@NotNull String name) {
        this.name = name;
    }

    public long getIngredientid() {
        return ingredientid;
    }

    public void setIngredientid(long ingredientid) {
        this.ingredientid = ingredientid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeIngredient> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeIngredient> recipes) {
        this.recipes = recipes;
    }
}