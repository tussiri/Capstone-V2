package org.launchcode.LiftoffRecipeProject.controllers;

import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.UserAuthenticationException;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.launchcode.LiftoffRecipeProject.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthenticationController(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService,
                                    AuthenticationManager authenticationManager, UserRepository userRepository,
                                    UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenUtil jwtTokenUtil1) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil1;
    }

    @PostMapping(value="/register")
    public ResponseEntity<ResponseWrapper<UserDTO>> register(@Valid @RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        UserDTO registeredUserDTO = userService.registerUser(userDTO);
        return ResponseUtil.wrapResponse(registeredUserDTO, HttpStatus.CREATED, "User created successfully. JWT is: " + registeredUserDTO.getToken());

    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<UserDTO>> loginUser(@RequestBody LoginDTO loginDTO) throws UserAuthenticationException {
        UserDTO userDTO = null;
        try {
            userDTO = userService.loginUser(loginDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseUtil.wrapResponse(userDTO, HttpStatus.OK, "Login successful. JWT is: " + userDTO.getToken());
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ResponseWrapper<String>> refreshToken(@RequestHeader("Authorization") String expiredToken) {
        String token = extractToken(expiredToken);
        if (expiredToken != null && expiredToken.startsWith("Bearer ")) {
            expiredToken = expiredToken.substring(7);
            if (jwtTokenUtil.canRefreshToken(expiredToken)) {
                String newToken = jwtTokenUtil.generateTokenFromExpiredToken(expiredToken);
                return ResponseUtil.wrapResponse(newToken, HttpStatus.OK, "Token refreshed successfully.");
            } else {
                return ResponseUtil.wrapResponse(null, HttpStatus.UNAUTHORIZED, "Unable to refresh token.");
            }
        } else {
            return ResponseUtil.wrapResponse(null, HttpStatus.BAD_REQUEST, "Invalid token format.");
        }
    }
    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}