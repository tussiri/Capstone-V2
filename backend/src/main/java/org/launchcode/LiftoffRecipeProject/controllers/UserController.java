package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.*;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.launchcode.LiftoffRecipeProject.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<UserDTO> userDTOs = userService.getAllUsers(pageable);
        return ResponseUtil.wrapResponse(userDTOs, HttpStatus.OK, "All users returned successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<UserDTO>> getUser(@PathVariable Integer userId) {
        UserDTO userDTO = userService.getUser(userId);
        return ResponseUtil.wrapResponse(userDTO, HttpStatus.OK, "User retrieved successfully");
    }


    @GetMapping("/with-recipes")
    public ResponseEntity<ResponseWrapper<Page<UserWithRecipesDTO>>> getAllUsersWithRecipes(Pageable pageable) {
        Page<UserWithRecipesDTO> userWithRecipesDTOs = userService.getAllUsersWithRecipes(pageable);
        return ResponseUtil.wrapResponse(userWithRecipesDTOs, HttpStatus.OK, "All users with recipes retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UpdateUserDTO>> updateUser(@PathVariable Integer id, @Valid @RequestBody UpdateUserDTO updatedUser) {
        UpdateUserDTO updateUserDTO = userService.updateUser(id, updatedUser);
        return ResponseUtil.wrapResponse(updateUserDTO, HttpStatus.OK, "User updated successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId, @RequestParam(defaultValue = "true") Boolean deleteRecipes) {
        try {
            userService.deleteUser(userId, deleteRecipes);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}/account")
    public ResponseEntity<ResponseWrapper<UpdateUserDTO>> updateAccountInfo(
            @PathVariable Integer userId,
            @Valid @RequestBody UpdateUserDTO updatedUser
    ) {
        UpdateUserDTO updateUserDTO = userService.updateUser(userId, updatedUser);
        return ResponseUtil.wrapResponse(updateUserDTO, HttpStatus.OK, "Account information updated successfully");
    }

    @PostMapping("/{userId}/favorites/{recipeId}")
    public ResponseEntity<ResponseWrapper<UserFavoriteRecipeDTO>>addFavorite(@PathVariable Integer userId, @PathVariable Integer recipeId){
        System.out.println("Favorite Recipe Request Received");
        userService.addFavorite(userId, recipeId);
        UserFavoriteRecipeDTO userFavoriteRecipeDTO = userService.getFavoriteRecipes(userId);
        return ResponseUtil.wrapResponse(userFavoriteRecipeDTO, HttpStatus.OK, "Favorited recipe successfully");
    }

    @DeleteMapping("/{userId}/favorites/{recipeId}")
    public ResponseEntity<ResponseWrapper<UserFavoriteRecipeDTO>> removeFavorite(@PathVariable Integer userId, @PathVariable Integer recipeId) {
        System.out.println("Unfavorite Recipe Request Received");
        userService.removeFavorite(userId, recipeId);
        UserFavoriteRecipeDTO userFavoriteRecipeDTO = userService.getFavoriteRecipes(userId);
        return ResponseUtil.wrapResponse(userFavoriteRecipeDTO, HttpStatus.OK, "Removed favorite recipe correctly");
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<ResponseWrapper<List<Recipe>>> getFavorites(@PathVariable Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseUtil.wrapResponse(user.getFavoriteRecipes(), HttpStatus.OK, "Favorite Recipes retrieved successfully.");
    }


}