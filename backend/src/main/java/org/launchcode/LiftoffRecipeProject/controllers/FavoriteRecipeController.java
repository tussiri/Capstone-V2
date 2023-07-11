package org.launchcode.LiftoffRecipeProject.controllers;

import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.data.FavoriteRepository;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Favorite;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.services.FavoriteRecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorites")
public class FavoriteRecipeController {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RecipeRepository recipeRepository;

    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    public static final Logger logger = LoggerFactory.getLogger(FavoriteRecipeController.class);


    @PostMapping("/{userId}/{recipeId}")
    public ResponseEntity<ResponseWrapper<Favorite>> addFavorite(@PathVariable Integer userId, @PathVariable Integer recipeId) {
        Favorite favorite = favoriteRecipeService.addFavorite(userId, recipeId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "Favorite added successfully", favorite), HttpStatus.CREATED);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteFavorite(@PathVariable Integer favoriteId) {
        favoriteRecipeService.deleteFavorite(favoriteId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Favorite deleted successfully"), HttpStatus.OK);
    }

//    @DeleteMapping("/user/{userId}/recipe/{recipeId}")
//    public ResponseEntity<ResponseWrapper<Void>> deleteFavoriteByUserAndRecipe(@PathVariable Integer userId, @PathVariable Integer recipeId) {
//        favoriteRecipeService.deleteFavoriteByUserAndRecipe(userId, recipeId);
//        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Favorite deleted successfully"), HttpStatus.OK);
//    }

    @GetMapping("/user/{userId}/recipe/{recipeId}")
    public ResponseEntity<ResponseWrapper<Favorite>> getFavoriteByUserAndRecipe(@PathVariable Integer userId, @PathVariable Integer recipeId) {
        Optional<Favorite> favorite = favoriteRecipeService.getFavoriteByUserAndRecipe(userId, recipeId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Favorite retrieved successfully", favorite.orElse(null)), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseWrapper<List<Favorite>>> getFavoritesByUser(@PathVariable Integer userId) {
        List<Favorite> favorites = favoriteRepository.findByUserIdWithRecipes(userId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Favorites retrieved successfully", favorites), HttpStatus.OK);
    }

}

