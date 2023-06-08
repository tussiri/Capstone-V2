package org.launchcode.LiftoffRecipeProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class RecipeDTO {

    private Integer id;

    @NotBlank(message = "Recipe name required")
    @Size(min=1, max=100, message = "Recipe name must be between 1 and 100 characters")
    private String name;

    private String description;

    @NotBlank(message = "Recipe category required")
    private String category;

    @NotEmpty(message = "Ingredients required")
    @Size(min=1, message = "Ingredients must have at least 1 item")
    private List<String> ingredients;

    @NotBlank(message = "Directions required")
    @Size(min=1, message = "Directions must have at least 1 step")
    private String directions;

    @NotNull(message = "Time is required")
    private Integer time;

    private boolean favorite;

    private String picture;

    private List<String> allergens;

    private Double rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}