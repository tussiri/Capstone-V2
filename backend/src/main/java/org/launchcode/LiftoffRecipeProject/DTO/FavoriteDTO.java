package org.launchcode.LiftoffRecipeProject.DTO;

import org.launchcode.LiftoffRecipeProject.models.Favorite;

public class FavoriteDTO {

    private Integer id;
    private Integer userId;
    private Integer recipeId;

    public FavoriteDTO(Favorite favorite) {
        this.id = favorite.getId();
        this.userId = favorite.getUser().getId();
        this.recipeId = favorite.getRecipe().getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }
}
