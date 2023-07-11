package org.launchcode.LiftoffRecipeProject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe extends AbstractEntity {

    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name="recipe_ingredients", joinColumns =@JoinColumn(name="recipe_id"),
    inverseJoinColumns =@JoinColumn(name="ingredient_id"))
    private List<Ingredient> ingredients;

//    @ElementCollection
//    private List<String>ingredients;

    @Column(name="directions", columnDefinition="MEDIUMTEXT")
    private String directions;

    @Column(nullable = false)
    private int time;

    @Column(nullable = true)
    private Boolean favorite;

    @ManyToMany(mappedBy = "favoriteRecipes")
    @JsonIgnore
    private List<User> favoritedByUsers = new ArrayList<>();

    @Column(columnDefinition="MEDIUMTEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    private String picture;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> allergens;

    private Double rating;

    @JsonIgnore
    @OneToMany(mappedBy="recipe", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Recipe() {
    }

    public Recipe(String name, List<Ingredient> ingredients, String directions, int time, Boolean favorite, String description, String category, String picture, List<String> allergens) {
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

    // Getters and setters


    public Boolean getFavorite() {
        return favorite;
    }

    public List<User> getFavoritedByUsers() {
        return favoritedByUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
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

    public Boolean isFavorite() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}