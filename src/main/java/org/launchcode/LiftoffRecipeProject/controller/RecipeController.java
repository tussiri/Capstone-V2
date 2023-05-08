package org.launchcode.LiftoffRecipeProject.controller;

import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.RecipeNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

//    TODO: Add search requests
//    TODO: Add sorting functionality to the getAllRecipes method


    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable int id) {
       Recipe recipe = recipeRepository.findById(id).orElseThrow(()->new RecipeNotFoundException("Recipe not found with id: " + id));
       return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    //Search by cooking time
    @GetMapping("/search")
    public ResponseEntity <List<Recipe>> searchRecipes(@RequestParam(required = false) String category, @RequestParam(required = false) String ingredients, @RequestParam(required = false) String keyword){
        List<Recipe> recipes = recipeRepository.findAll();
        List<Recipe> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            boolean match=true;

            if(category != null && !recipe.getCategory().equals(category)){
                match=false;
            }
            //using stream here to process the ingredients data using functional programming. This allows us to perform multiple operations on the array of ingredients.
            if (ingredients != null) {
                boolean ingredientFound = recipe.getIngredients().stream()
                        .anyMatch(ingredient -> ingredient.toLowerCase().contains(ingredients.toLowerCase()));
                if (!ingredientFound) {
                    match = false;
                }
            }
            if(keyword !=null && !(
                    recipe.getName().toLowerCase().contains(keyword.toLowerCase())|| recipe.getDescription().toLowerCase().contains(keyword.toLowerCase()))){
                match=false;
            }
            if(match){
                filteredRecipes.add(recipe);
            }
        }
        return new ResponseEntity<>(filteredRecipes,HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Recipe> createRecipe(@PathVariable Integer userId, @Valid @RequestBody Recipe recipe) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        recipe.setUser(user); // set the user field on the recipe object
        Recipe newRecipe = recipeRepository.save(recipe);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            Recipe updatedRecipe = existingRecipe.get();
            updatedRecipe.setName(recipe.getName());
            updatedRecipe.setIngredients(recipe.getIngredients());
            updatedRecipe.setDirections(recipe.getDirections());
            updatedRecipe.setTime(recipe.getTime());
            updatedRecipe.setDescription(recipe.getDescription());
            updatedRecipe.setCategory(recipe.getCategory());
            updatedRecipe.setPicture(recipe.getPicture());
            updatedRecipe.setAllergens(recipe.getAllergens());
            recipeRepository.save(updatedRecipe);
            return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable int id, Integer userId) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
