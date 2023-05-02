package org.launchcode.LiftoffRecipeProject.controller;

import jakarta.transaction.Transactional;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

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

    @Transactional
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @Transactional
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

    @Transactional
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

}
