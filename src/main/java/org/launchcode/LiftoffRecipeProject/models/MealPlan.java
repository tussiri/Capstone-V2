//package org.launchcode.LiftoffRecipeProject.models;
//
//import jakarta.persistence.*;
//
//import java.util.Set;
//
//@Entity
//public class MealPlan extends AbstractEntity{
//
//    @ManyToOne
//    @JoinColumn(name="user_id")
//    private User user;
//
//    private String name;
//
//    @ManyToMany
//    @JoinTable(
//            name = "meal_plan_recipes",
//            joinColumns = @JoinColumn(name = "meal_plan_id"),
//            inverseJoinColumns = @JoinColumn(name = "recipe_id")
//    )
//    private Set<Recipe>recipes;
//
//    public MealPlan(){}
//
//    public MealPlan(String name, User user){
//        this.name = name;
//        this.user = user;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Set<Recipe> getRecipes() {
//        return recipes;
//    }
//
//    public void setRecipes(Set<Recipe> recipes) {
//        this.recipes = recipes;
//    }
//}
