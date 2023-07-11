package org.launchcode.LiftoffRecipeProject.controllers;

import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Favorite;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.services.FavoriteRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/favorites")
public class FavoriteRecipeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private FavoriteRecipeService favoriteRecipeService;

    @PostMapping("/{userId}/{recipeId}")
    public ResponseEntity<ResponseWrapper<Favorite>> addFavorite(@PathVariable Integer userId, @PathVariable Integer recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        Favorite favorite = favoriteRecipeService.addFavorite(user, recipe);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "Favorite added successfully", favorite), HttpStatus.CREATED);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteFavorite(@PathVariable Integer favoriteId) {
        favoriteRecipeService.deleteFavorite(favoriteId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Favorite deleted successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/recipe/{recipeId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteFavoriteByUserAndRecipe(@PathVariable Integer userId, @PathVariable Integer recipeId) {
        favoriteRecipeService.deleteFavoriteByUserAndRecipe(userId, recipeId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Favorite deleted successfully"), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/recipe/{recipeId}")
    public ResponseEntity<ResponseWrapper<Favorite>> getFavoriteByUserAndRecipe(@PathVariable Integer userId, @PathVariable Integer recipeId) {
        Optional<Favorite> favorite = favoriteRecipeService.getFavoriteByUserAndRecipe(userId, recipeId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Favorite retrieved successfully", favorite.orElse(null)), HttpStatus.OK);
    }
}

