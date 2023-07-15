package org.launchcode.LiftoffRecipeProject.DTO;

import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;

import java.util.List;

public class UserFavoriteRecipeDTO {
    private User user;
    private List<Recipe> favoriteRecipes;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Recipe> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<Recipe> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }
}
