package org.launchcode.LiftoffRecipeProject.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "recipes")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Recipe.class)
public class Recipe extends AbstractEntity {

    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    @JsonManagedReference
    private List<Ingredient> ingredients;

    @Column(name = "directions", columnDefinition = "MEDIUMTEXT")
    private String directions;

    @Column(nullable = false)
    private int time;

    @Column(nullable = true)
    private Boolean favorite=false;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    private String picture;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> allergens;

    private Double rating;

    @JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToMany(mappedBy = "favoriteRecipes")
    @JsonBackReference(value= "user-favoriteRecipes")
    private List<User> favoritedByUser = new ArrayList<>();

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

    public List<User> getFavoritedByUser() {
        return favoritedByUser;
    }

    public void setFavoritedByUser(List<User> favoritedByUser) {
        this.favoritedByUser = favoritedByUser;
    }

    public void addFavoritedByUser(User user) {
        this.favoritedByUser.add(user);
        user.getFavoriteRecipes().add(this);
    }

    public void removeFavoritedByUser(User user) {
        this.favoritedByUser.remove(user);
        user.getFavoriteRecipes().remove(this);
    }
}