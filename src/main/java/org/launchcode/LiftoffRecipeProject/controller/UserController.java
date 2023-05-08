package org.launchcode.LiftoffRecipeProject.controller;

import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@Controller
@RestController
@RequestMapping("/users")
public class UserController {

//    TODO: Add functionality to add or remove a users favorite on a recipe
//    TODO: Add code to include a list of favorite recipes and add and remove favorites
//    TODO: Add functionality to rate and review a recipe
//    TODO: Add functionality for login and viewing all recipes and or favorite recipes

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/recipes")
    public ResponseEntity <List<Recipe>> getUserRecipes(@PathVariable Integer userId){
        Optional<User>user = userRepository.findById(userId);
//        User user = userRepository.findById(userId).orElse(null);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Recipe> recipes = user.get().getRecipes();
        return new ResponseEntity<>(recipes,HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/favorites/{recipeId}")
    public ResponseEntity<Void> addFavoriteRecipe(@PathVariable Integer userId, @PathVariable Integer recipeId){
        User user = userRepository.findById(userId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if(user == null||recipe==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.addFavoriteRecipe(recipe);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

        userRepository.save(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}/favorites/{recipeId}")
    public ResponseEntity<Void> removeFavoriteRecipe(@PathVariable Integer userId, @PathVariable Integer recipeId) {
        User user = userRepository.findById(userId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (user == null || recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.removeFavoriteRecipe(recipe);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
