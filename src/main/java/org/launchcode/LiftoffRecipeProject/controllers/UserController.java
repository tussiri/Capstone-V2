package org.launchcode.LiftoffRecipeProject.controllers;

import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserWithRecipesDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

//    @PostMapping
//    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
//        User user = new User(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getDateOfBirth());
//        userRepository.save(user);
//
//        userDTO.setId(user.getId());
//        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
//    }
@GetMapping
public ResponseEntity<ResponseWrapper<List<UserDTO>>> getAllUsers() {
    List<User> users = (List<User>) userRepository.findAll();
    List<UserDTO> userDTOs = users.stream().map(user -> {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setDateOfBirth(user.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return userDTO;
    }).collect(Collectors.toList());

    return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All users returned successfully", userDTOs), HttpStatus.OK);
}

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<UserDTO>> getUser(@PathVariable Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setDateOfBirth(user.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            return new ResponseEntity<>(new ResponseWrapper<>("User retrieved successfully", userDTO), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }


    @GetMapping("/with-recipes")
    public ResponseEntity<ResponseWrapper<List<UserWithRecipesDTO>>> getAllUsersWithRecipes() {
        List<User> users = (List<User>) userRepository.findAll();

        List<UserWithRecipesDTO> userWithRecipesDTOs = users.stream().map(user -> {
            UserWithRecipesDTO dto = new UserWithRecipesDTO();
            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setDateOfBirth(user.getDateOfBirth());
            dto.setRecipes(user.getRecipes());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All users with recipes retrieved successfully", userWithRecipesDTOs), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UserDTO>> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
    Optional <User> optionalUser = userRepository.findById(id);
    if(optionalUser.isPresent()) {
        User user = optionalUser.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setDateOfBirth(user.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "User updated successfully", userDTO), HttpStatus.OK);
    }else{
        throw new ResourceNotFoundException("User not found");
    }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteUser(@PathVariable Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(),"User deleted successfully"),HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
