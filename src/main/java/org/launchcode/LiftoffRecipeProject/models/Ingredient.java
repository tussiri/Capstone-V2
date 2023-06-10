package org.launchcode.LiftoffRecipeProject.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Ingredient extends AbstractEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(unique = false)
    private String name;

    private String quantity;

    @ManyToMany(mappedBy="ingredients")
    private List<Recipe> recipes;

    public Ingredient(){}

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}