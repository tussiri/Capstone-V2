package org.launchcode.LiftoffRecipeProject.services;

import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserWithRecipesDTO;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.BadRequestException;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.UnauthorizedException;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

//    @Autowired
////    private SessionUtil sessionUtil;

    public UserDTO getUser(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return mapUserToUserDTO(user);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public Page<UserDTO>getAllUsers(Pageable pageable){
        Page<User>users = userRepository.findAll(pageable);
        return users.map(this::mapUserToUserDTO);
    }
    public Page<UserWithRecipesDTO> getAllUsersWithRecipes(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::mapUserToUserWithRecipesDTO);
    }

    public UserDTO updateUser(Integer userId, UserDTO updatedUserDTO){
        Optional <User>optionalUser=userRepository.findById(userId);

        if(!optionalUser.isPresent()){
            throw new ResourceNotFoundException("User not found");
        }

        User user = optionalUser.get();

        user.setFirstName(updatedUserDTO.getFirstName());
        user.setLastName(updatedUserDTO.getLastName());
        user.setEmail(updatedUserDTO.getEmail());
        user.setDateOfBirth(updatedUserDTO.getDateOfBirth());

        user.setPassword(bCryptPasswordEncoder.encode(updatedUserDTO.getPassword()));
        userRepository.save(user);
        return mapUserToUserDTO(user);
    }

    public UserDTO loginUser(LoginDTO loginDTO) throws Exception {
        authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user != null) {
            UserDTO userDTO = mapUserToUserDTO(user);
            userDTO.setToken(jwt);
            return userDTO;
        } else {
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public UserDTO registerUser(UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            throw new BadRequestException("User already exists");
        }

        User newUser = new User(
                userDTO.getEmail(),
                bCryptPasswordEncoder.encode(userDTO.getPassword()),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getDateOfBirth()
        );

        User registeredUser = userRepository.save(newUser);
        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

//        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getEmail());
//        String token = jwtTokenUtil.generateToken(userDetails);

        UserDTO mappedUserDTO = mapUserToUserDTO(registeredUser);
//        mappedUserDTO.setSessionToken(sessionToken);
        mappedUserDTO.setToken(token);

        return mappedUserDTO;
    }

    public void deleteUser (Integer userId){
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    private UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        return userDTO;
    }

    private UserWithRecipesDTO mapUserToUserWithRecipesDTO(User user) {
        UserWithRecipesDTO userWithRecipesDTO = new UserWithRecipesDTO();
        userWithRecipesDTO.setId(user.getId());
        userWithRecipesDTO.setFirstName(user.getFirstName());
        userWithRecipesDTO.setLastName(user.getLastName());
        userWithRecipesDTO.setEmail(user.getEmail());
        userWithRecipesDTO.setDateOfBirth(user.getDateOfBirth());
        userWithRecipesDTO.setRecipes(user.getRecipes());
        return userWithRecipesDTO;
    }
}