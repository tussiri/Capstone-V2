package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
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

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper<UserDTO>> register(@Valid @RequestBody UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.BAD_REQUEST.value(), "User already exists", null), HttpStatus.BAD_REQUEST);
        }

        User newUser = new User(
                userDTO.getEmail(),
                bCryptPasswordEncoder.encode(userDTO.getPassword()),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getDateOfBirth()
        );

        User registeredUser = userRepository.save(newUser);
        Integer userId = registeredUser.getId();

        System.out.println("Received UserDTO: " + userDTO);

        UserDTO mappedUserDTO = new UserDTO();
        mappedUserDTO.setId(registeredUser.getId());
        mappedUserDTO.setEmail(registeredUser.getEmail());
        mappedUserDTO.setFirstName(registeredUser.getFirstName());
        mappedUserDTO.setLastName(registeredUser.getLastName());
        mappedUserDTO.setDateOfBirth(registeredUser.getDateOfBirth());

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "User registered successfully", mappedUserDTO), HttpStatus.CREATED);
    }
//
//        System.out.println("Constructed User: " + newUser);
//        System.out.println("Received UserDTO: ");
//        System.out.println("Email: " + userDTO.getEmail());
//        System.out.println("Password: " + userDTO.getPassword());
//        System.out.println("First Name: " + userDTO.getFirstName());
//        System.out.println("Last Name: " + userDTO.getLastName());
//        System.out.println("Date of Birth: " + userDTO.getDateOfBirth());
//        System.out.println("Saved User: " + newUser);

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<UserDTO>> loginUser(@RequestBody LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(null);
            userDTO.setDateOfBirth(user.getDateOfBirth());

            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Login successful", userDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password", null), HttpStatus.UNAUTHORIZED);
        }
    }
}
