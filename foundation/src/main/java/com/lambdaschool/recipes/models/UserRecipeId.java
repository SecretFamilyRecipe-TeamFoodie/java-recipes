package com.lambdaschool.recipes.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserRecipeId implements Serializable {
    private long user;
    private long recipe;

    public UserRecipeId() {
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getRecipe() {
        return recipe;
    }

    public void setRecipe(long recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecipeId that = (UserRecipeId) o;
        return getRecipe() == that.getRecipe() &&
                getUser() == that.getUser();
    }

    @Override
    public int hashCode() {
        return 135;
    }
}
