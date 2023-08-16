package org.launchcode.LiftoffRecipeProject.controllers;

import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.launchcode.LiftoffRecipeProject.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private JwtTokenUtil jwtTokenUtil;

    public AuthenticationManager authenticationManager;
    private UserService userService;



    @Autowired
    public AuthenticationController(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService, AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping(value="/register")
    public ResponseEntity<ResponseWrapper<UserDTO>> register(@Valid @RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        UserDTO registeredUserDTO = userService.registerUser(userDTO);
        return ResponseUtil.wrapResponse(registeredUserDTO, HttpStatus.CREATED, "User created successfully. JWT is: " + registeredUserDTO.getToken());

    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<UserDTO>> loginUser(@RequestBody LoginDTO loginDTO) throws Exception {
        UserDTO userDTO = userService.loginUser(loginDTO);
        return ResponseUtil.wrapResponse(userDTO, HttpStatus.OK, "Login successful. JWT is: " + userDTO.getToken());
    }

}