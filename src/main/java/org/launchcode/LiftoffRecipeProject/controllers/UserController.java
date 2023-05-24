package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserWithRecipesDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDTO> userDTOs = users.map(this::mapToUserDTO);

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All users returned successfully", userDTOs), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<UserDTO>> getUser(@PathVariable Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = mapToUserDTO(user);

            return new ResponseEntity<>(new ResponseWrapper<>("User retrieved successfully", userDTO), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @GetMapping("/with-recipes")
    public ResponseEntity<ResponseWrapper<Page<UserWithRecipesDTO>>> getAllUsersWithRecipes(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserWithRecipesDTO> userWithRecipesDTOs = users.map(this::mapToUserWithRecipesDTO);

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All users with recipes retrieved successfully", userWithRecipesDTOs), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UserDTO>> updateUser(@Valid @PathVariable Integer id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setDateOfBirth(updatedUser.getDateOfBirth());
            userRepository.save(user);

            UserDTO userDTO = mapToUserDTO(user);

            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "User updated successfully", userDTO), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteUser(@PathVariable Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully"), HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        return userDTO;
    }

    private UserWithRecipesDTO mapToUserWithRecipesDTO(User user) {
        UserWithRecipesDTO dto = new UserWithRecipesDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setRecipes(user.getRecipes());
        return dto;
    }
}