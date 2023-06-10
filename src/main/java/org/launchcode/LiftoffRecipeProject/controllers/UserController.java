package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserWithRecipesDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.launchcode.LiftoffRecipeProject.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


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
    public ResponseEntity<ResponseWrapper<UserDTO>> updateUser(@PathVariable Integer id, @Valid @RequestBody UserDTO updatedUser) {
        UserDTO userDTO = userService.updateUser(id, updatedUser);
        return ResponseUtil.wrapResponse(userDTO, HttpStatus.OK, "User updated successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully"), HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{userId}/account")
    public ResponseEntity<ResponseWrapper<UserDTO>> updateAccountInfo(
            @PathVariable Integer userId,
            @Valid @RequestBody UserDTO updatedUser
    ) {
        UserDTO userDTO = userService.updateUser(userId, updatedUser);
        return ResponseUtil.wrapResponse(userDTO, HttpStatus.OK, "Account information updated successfully");
    }
}