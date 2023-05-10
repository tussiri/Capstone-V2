package org.launchcode.LiftoffRecipeProject.controllers;

import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = (List<Recipe>) recipeRepository.findAll();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Integer id) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setName(recipe.getName());
        recipeDTO.setCreatedBy(recipe.getUser().getFirstName() + " " + recipe.getUser().getLastName());

        return new ResponseEntity<>(recipeDTO, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Recipe> createRecipe(@PathVariable Integer userId, @Valid @RequestBody Recipe recipe) {
        try{
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        recipe.setUser(user); // set the user field on the recipe object
        Recipe newRecipe = recipeRepository.save(recipe);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }catch (Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Integer id, @RequestBody Recipe updatedRecipe) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (recipeOptional.isPresent()) {
            Recipe existingRecipe = recipeOptional.get();
            existingRecipe.setName(updatedRecipe.getName());
            existingRecipe.setIngredients(updatedRecipe.getIngredients());
            existingRecipe.setDirections(updatedRecipe.getDirections());
            existingRecipe.setTime(updatedRecipe.getTime());
            existingRecipe.setFavorite(updatedRecipe.getFavorite());
            existingRecipe.setDescription(updatedRecipe.getDescription());
            existingRecipe.setCategory(updatedRecipe.getCategory());
            existingRecipe.setPicture(updatedRecipe.getPicture());
            existingRecipe.setAllergens(updatedRecipe.getAllergens());
            recipeRepository.save(existingRecipe);
            return new ResponseEntity<>(existingRecipe, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Integer id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
