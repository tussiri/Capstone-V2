package org.launchcode.LiftoffRecipeProject.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.repository.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

//    TODO: Add search requests
//    TODO: Add custom method for searching
//    TODO: Add sorting functionality to the getAllRecipes method


    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity <List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = (List<Recipe>) recipeRepository.findAll();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable int id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            return new ResponseEntity<>(recipe.get(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @Transactional
//    @PostMapping("/{id}")
//    public ResponseEntity<Recipe> createRecipe(@PathVariable Integer id, @Valid @RequestBody Recipe recipe) {
//        User user = userRepository.findById(id).orElse(null);
//        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        recipe.setUser(user);
//        Recipe newRecipe = recipeRepository.save(recipe);
//        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
//    }

    @PostMapping("/{userId}")
    public ResponseEntity<Recipe> createRecipe(@PathVariable Integer userId, @Valid @RequestBody Recipe recipe) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        recipe.setUser(user); // set the user field on the recipe object
        Recipe newRecipe = recipeRepository.save(recipe);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            Recipe updatedRecipe = existingRecipe.get();
            updatedRecipe.setName(recipe.getName());
            updatedRecipe.setIngredients(recipe.getIngredients());
            updatedRecipe.setDirections(recipe.getDirections());
            updatedRecipe.setTime(recipe.getTime());
            updatedRecipe.setFavorite(recipe.getFavorite());
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

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable int id) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
