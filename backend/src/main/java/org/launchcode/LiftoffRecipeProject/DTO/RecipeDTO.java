package org.launchcode.LiftoffRecipeProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


/**
 * Data Transfer Object representing a recipe.
 * This DTO is used to transfer recipe data between the application layers.
 * Represents a recipe with all its associated details.
 * Fields:
 * id: Unique identifier for the recipe.
 * name: Name of the recipe.
 * description: A brief description or summary of the recipe.
 * category: The category or type of the recipe (e.g., dessert, main course).
 * ingredients: A list of ingredients required for the recipe.
 * directions: Step-by-step directions to prepare the recipe.
 * time: The time required to prepare and cook the recipe.
 * favorite: A boolean indicating if the recipe is marked as favorite.
 * picture: A link or path to the recipe's image.
 * allergens: A list of potential allergens present in the recipe.
 * rating: The average rating of the recipe.
 */
public class RecipeDTO {

    private Integer id;

    private Integer userId;

    @NotBlank(message = "Recipe name required")
    @Size(min=1, max=100, message = "Recipe name must be between 1 and 100 characters")
    private String name;

    private String description;

    @NotBlank(message = "Recipe category required")
    private String category;

    @NotEmpty(message = "Ingredients required")
    @Size(min=1, message = "Ingredients must have at least 1 item")
    private List<IngredientDTO> ingredients = new ArrayList<>();



    private List<RecipeIngredientDTO> recipeIngredients;

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

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<RecipeIngredientDTO> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredientDTO> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}