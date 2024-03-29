package org.launchcode.LiftoffRecipeProject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "reviews")
public class Review extends AbstractEntity {

    private String comment;

    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true )
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    public Review() {}
    public Review(String comment, Integer rating, User user, Recipe recipe) {
        this.comment = comment;
        this.rating = rating;
        this.user = user;
        this.recipe = recipe;
    }

    public Integer getId(){
        return super.getId();
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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
}