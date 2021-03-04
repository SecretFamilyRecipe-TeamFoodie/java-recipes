package com.lambdaschool.recipes.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "userrecipes")
@IdClass(UserRecipeId.class)
public class UserRecipe extends Auditable implements Serializable {
    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = {"recipe", "ownerrecipes", "roles", "useremails", "guestrecipes", "primaryemail"}, allowSetters = true)
    private User user;

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "recipeid")
    @JsonIgnoreProperties(value = "guests", allowSetters = true)
    private Recipe recipe;

    public UserRecipe() {
    }

    public UserRecipe(@NotNull User user, @NotNull Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecipe that = (UserRecipe) o;
        return Objects.equals(getRecipe().getRecipeid(), that.getRecipe().getRecipeid()) &&
                Objects.equals(getUser().getUserid(), that.getUser().getUserid());
    }

    @Override
    public int hashCode() {
        return 134;
    }
}
