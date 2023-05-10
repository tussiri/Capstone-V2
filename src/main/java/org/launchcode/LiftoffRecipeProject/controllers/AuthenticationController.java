package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.models.User;
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

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationController(){
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO){
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println("Received UserDTO: " + userDTO);

        User newUser = new User(
                userDTO.getEmail(),
                bCryptPasswordEncoder.encode(userDTO.getPassword()),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getDateOfBirth()
        );
        System.out.println("Constructed User: " + newUser);
        System.out.println("Received UserDTO: ");
        System.out.println("Email: " + userDTO.getEmail());
        System.out.println("Password: " + userDTO.getPassword());
        System.out.println("First Name: " + userDTO.getFirstName());
        System.out.println("Last Name: " + userDTO.getLastName());
        System.out.println("Date of Birth: " + userDTO.getDateOfBirth());

        User registeredUser = userRepository.save(newUser);
        System.out.println("Saved User: " + newUser);

        Integer userId=registeredUser.getId();

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }




}
