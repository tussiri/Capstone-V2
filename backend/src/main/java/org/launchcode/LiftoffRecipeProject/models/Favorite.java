package org.launchcode.LiftoffRecipeProject.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
public class Favorite extends AbstractEntity{

    @ManyToOne
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    private User user;

    @ManyToOne
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
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
