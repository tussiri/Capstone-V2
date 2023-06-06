package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserWithRecipesDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.InvalidSessionException;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.security.SessionUtil;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.launchcode.LiftoffRecipeProject.util.ResponseUtil;
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

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDTO> userDTOs = users.map(this::mapToUserDTO);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All users returned successfully", userDTOs), HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<UserDTO>> getUser(@PathVariable Integer userId, HttpServletRequest request) {
        try {
            User user = validateSessionAndGetUser(request);
            Optional<User> optionalUser = userRepository.findById(userId);

            if (!user.getId().equals(userId)) {
                return new ResponseEntity<>(
                        new ResponseWrapper<>(HttpStatus.FORBIDDEN.value(), "Access denied", null),
                        HttpStatus.FORBIDDEN);
            }

            if (optionalUser.isPresent()) {
                user = optionalUser.get();
                UserDTO userDTO = mapToUserDTO(user);

                return new ResponseEntity<>(new ResponseWrapper<>("User retrieved successfully", userDTO), HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("User not found");
            }
        } catch (InvalidSessionException e) {
            return new ResponseEntity<>(
                    new ResponseWrapper<>(HttpStatus.FORBIDDEN.value(), e.getMessage(), null),
                    HttpStatus.FORBIDDEN
            );
        }
    }

    @GetMapping("/with-recipes")
    public ResponseEntity<ResponseWrapper<Page<UserWithRecipesDTO>>> getAllUsersWithRecipes(Pageable pageable) {
        Page<UserWithRecipesDTO> userWithRecipesDTOs = userService.getAllUsersWithRecipes(pageable);
        return ResponseUtil.wrapResponse(userWithRecipesDTOs, HttpStatus.OK, "All users with recipes retrieved successfully");
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


    private User validateSessionAndGetUser(HttpServletRequest request) {
        String sessionToken = request.getHeader("X-Session-Token");

        if (!sessionUtil.isValidSession(sessionToken)) {
            throw new InvalidSessionException("Invalid session or session expired");
        }
        return sessionUtil.getUserFromSession(sessionToken);

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