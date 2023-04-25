package org.launchcode.LiftoffRecipeProject.models;

import java.util.List;

public class Recipe extends AbstractEntity {

    //todo: establish DB relationships with other classes
    private String name;

    private List<String> ingredients;
    private String directions;
    private int time;
    private Boolean favorite;
    private String description;
    private String category;
    private String picture;
    private List<String> allergens;

    public Recipe(String name, List<String> ingredients, String directions, int time, Boolean favorite, String description, String category, String picture, List<String> allergens) {
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
        this.time = time;
        this.favorite = favorite;
        this.description = description;
        this.category = category;
        this.picture = picture;
        this.allergens = allergens;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
