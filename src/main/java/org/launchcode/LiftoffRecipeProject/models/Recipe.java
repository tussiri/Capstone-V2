package org.launchcode.LiftoffRecipeProject.models;

import java.util.List;

public class Recipe extends AbstractEntity {

    //todo: establish DB relationships with other classes
    private String name;

    private List<String> ingredients;

    private String directions;

    private int time;

    public Recipe(String name, List<String> ingredients, String directions, int time) {
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
        this.time = time;
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
