package org.launchcode.LiftoffRecipeProject.controllers;

import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<RecipeDTO>>> getAllRecipes() {
        List<Recipe> recipes = (List<Recipe>) recipeRepository.findAll();
        List<RecipeDTO> recipeDTOs = recipes.stream().map(recipe -> {
            RecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setId(recipe.getId());
            recipeDTO.setName(recipe.getName());
            recipeDTO.setDescription(recipe.getDescription());
            recipeDTO.setCategory(recipe.getCategory());
            recipeDTO.setIngredients(recipe.getIngredients());
            recipeDTO.setDirections(recipe.getDirections());
            recipeDTO.setTime(recipe.getTime());
            recipeDTO.setFavorite(recipe.getFavorite());
            recipeDTO.setPicture(recipe.getPicture());
            recipeDTO.setAllergens(recipe.getAllergens());
            return recipeDTO;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All recipes returned successfully", recipeDTOs), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> getRecipe(@PathVariable Integer id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            RecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setId(recipe.getId());
            recipeDTO.setName(recipe.getName());
            recipeDTO.setDescription(recipe.getDescription());
            recipeDTO.setCategory(recipe.getCategory());
            recipeDTO.setIngredients(recipe.getIngredients());
            recipeDTO.setDirections(recipe.getDirections());
            recipeDTO.setTime(recipe.getTime());
            recipeDTO.setFavorite(recipe.getFavorite());
            recipeDTO.setPicture(recipe.getPicture());
            recipeDTO.setAllergens(recipe.getAllergens());
            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe retrieved successfully", recipeDTO), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> createRecipe(@PathVariable Integer userId, @Valid @RequestBody RecipeDTO recipeDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }

        User user = optionalUser.get();
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setCategory(recipeDTO.getCategory());
        recipe.setIngredients(recipeDTO.getIngredients());
        recipe.setDirections(recipeDTO.getDirections());
        recipe.setTime(recipeDTO.getTime());
        recipe.setFavorite(recipeDTO.isFavorite());
        recipe.setPicture(recipeDTO.getPicture());
        recipe.setAllergens(recipeDTO.getAllergens());
        recipe.setUser(user);
        Recipe savedRecipe = recipeRepository.save(recipe);

        RecipeDTO savedRecipeDTO = new RecipeDTO();
        savedRecipeDTO.setId(savedRecipe.getId());
        savedRecipeDTO.setName(savedRecipe.getName());
        savedRecipeDTO.setDescription(savedRecipe.getDescription());
        savedRecipeDTO.setCategory(savedRecipe.getCategory());
        savedRecipeDTO.setIngredients(savedRecipe.getIngredients());
        savedRecipeDTO.setDirections(savedRecipe.getDirections());
        savedRecipeDTO.setTime(savedRecipe.getTime());
        savedRecipeDTO.setFavorite(savedRecipe.getFavorite());
        savedRecipeDTO.setPicture(savedRecipe.getPicture());
        savedRecipeDTO.setAllergens(savedRecipe.getAllergens());

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "Recipe created successfully", savedRecipeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> updateRecipe(@PathVariable Integer id, @RequestBody Recipe updatedRecipe) {
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

            RecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setId(existingRecipe.getId());
            recipeDTO.setName(existingRecipe.getName());
            recipeDTO.setDescription(existingRecipe.getDescription());
            recipeDTO.setCategory(existingRecipe.getCategory());
            recipeDTO.setIngredients(existingRecipe.getIngredients());
            recipeDTO.setDirections(existingRecipe.getDirections());
            recipeDTO.setTime(existingRecipe.getTime());
            recipeDTO.setFavorite(existingRecipe.getFavorite());
            recipeDTO.setPicture(existingRecipe.getPicture());
            recipeDTO.setAllergens(existingRecipe.getAllergens());

            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe updated successfully", recipeDTO), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteRecipe(@PathVariable Integer id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Recipe deleted successfully", null), HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }
    }
}
