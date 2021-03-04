package com.lambdaschool.recipes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long recipeid;

    @NotNull
    private String name;

    private String source;

    private String instructions;

    private String category;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "recipe", allowSetters = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @ManyToOne
    @NotNull
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = {"ownerrecipes", "roles", "useremails", "guestrecipes", "primaryemail"}, allowSetters = true) // only vital information
    private User owner;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "recipe", allowSetters = true)
    private List<UserRecipe> guests = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(RecipeMinimal minimal)
    {
        name = minimal.getName();
        category = minimal.getCategory();
        source = minimal.getSource();
        instructions = minimal.getInstructions();
        if(minimal.getRecipeid() != 0) recipeid = minimal.getRecipeid();
    }

    public Recipe(@NotNull String name, String source, String instructions, String category) {
        this.name = name;
        this.source = source;
        this.instructions = instructions;
        this.category = category;
    }

    public long getRecipeid() {
        return recipeid;
    }

    public void setRecipeid(long recipeid) {
        this.recipeid = recipeid;
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

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<UserRecipe> getGuests() {
        return guests;
    }

    public void setGuests(List<UserRecipe> guests) {
        this.guests = guests;
    }
}