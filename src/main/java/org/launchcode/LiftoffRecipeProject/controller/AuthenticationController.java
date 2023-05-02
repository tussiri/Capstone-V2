package org.launchcode.LiftoffRecipeProject.controller;


import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO){
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode (userDTO.getPassword()));
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setDateOfBirth(userDTO.getDateOfBirth());
        userRepository.save(newUser);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }




}
