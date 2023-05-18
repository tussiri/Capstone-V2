package org.launchcode.LiftoffRecipeProject.controllers;

import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper<UserDTO>> register(@Valid @RequestBody UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>(
                    new ResponseWrapper<>(HttpStatus.BAD_REQUEST.value(), "User already exists", null),
                    HttpStatus.BAD_REQUEST);
        }

        User newUser = new User(
                userDTO.getEmail(),
                bCryptPasswordEncoder.encode(userDTO.getPassword()),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getDateOfBirth());


        User registeredUser = userRepository.save(newUser);
        Integer userId = registeredUser.getId();

        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        UserDTO mappedUserDTO = mapUserToUserDTO(registeredUser);
        mappedUserDTO.setToken(token);

        System.out.println("Received UserDTO: " + userDTO);

        return new ResponseEntity<>(
                new ResponseWrapper<>(HttpStatus.CREATED.value(), "User registered successfully", mappedUserDTO),
                HttpStatus.CREATED);
    }
    // Logs to see if the user details are being passed to the database.
    // System.out.println("Constructed User: " + newUser);
    // System.out.println("Received UserDTO: ");
    // System.out.println("Email: " + userDTO.getEmail());
    // System.out.println("Password: " + userDTO.getPassword());
    // System.out.println("First Name: " + userDTO.getFirstName());
    // System.out.println("Last Name: " + userDTO.getLastName());
    // System.out.println("Date of Birth: " + userDTO.getDateOfBirth());
    // System.out.println("Saved User: " + newUser);

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<UserDTO>> loginUser(@RequestBody LoginDTO loginDTO) throws Exception {
        authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user != null) {
            UserDTO userDTO = mapUserToUserDTO(user);
            userDTO.setToken(jwt);

            ResponseWrapper<UserDTO> responseWrapper = new ResponseWrapper<>(HttpStatus.OK.value(), "Login successful", userDTO);
//            responseWrapper.setToken(jwt);

            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ResponseWrapper<>(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password", null),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(null);
        userDTO.setDateOfBirth(user.getDateOfBirth());

        return userDTO;
    }
}