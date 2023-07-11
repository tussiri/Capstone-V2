package org.launchcode.LiftoffRecipeProject.models;

import jakarta.persistence.*;


@Entity
public class Favorite extends AbstractEntity{

    @ManyToOne
    private User user;

    @ManyToOne
    private Recipe recipe;

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
}
