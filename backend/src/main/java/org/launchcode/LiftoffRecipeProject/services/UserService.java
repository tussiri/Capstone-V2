package org.launchcode.LiftoffRecipeProject.services;

import org.launchcode.LiftoffRecipeProject.DTO.*;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.*;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
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

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private RecipeService recipeService;


//    @Autowired
////    private SessionUtil sessionUtil;

    public UserDTO getUser(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return mapUserToUserDTO(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::mapUserToUserDTO);
    }

    public Page<UserWithRecipesDTO> getAllUsersWithRecipes(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::mapUserToUserWithRecipesDTO);
    }

    public UpdateUserDTO updateUser(Integer userId, UpdateUserDTO updatedUserDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        User user = optionalUser.get();

        if (updatedUserDTO.getFirstName() != null && !updatedUserDTO.getFirstName().isEmpty()) {
            user.setFirstName(updatedUserDTO.getFirstName());
        }
//        user.setFirstName(updatedUserDTO.getFirstName());
//        user.setLastName(updatedUserDTO.getLastName());

        if (updatedUserDTO.getLastName() != null && !updatedUserDTO.getLastName().isEmpty()) {
            user.setLastName(updatedUserDTO.getLastName());
        }

        // Debugging: Print the password inside updateUser method
        System.out.println("Password inside updateUser method: " + updatedUserDTO.getPassword());

        if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(updatedUserDTO.getPassword()));

//            user.setPassword(bCryptPasswordEncoder.encode(updatedUserDTO.getPassword()));
        }

        // Debugging: Print the encoded password inside updateUser method
        System.out.println("Encoded password inside updateUser method: " + user.getPassword());

        userRepository.save(user);
        return mapUserToUpdateUserDTO(user);
    }


    public UserDTO loginUser(LoginDTO loginDTO) {
        authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user != null) {
            UserDTO userDTO = mapUserToUserDTO(user);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
            final String jwt = jwtTokenUtil.generateToken(userDetails, user.getId());
            userDTO.setToken(jwt);
            return userDTO;
        } else {
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    private void authenticate(String username, String password) throws UnauthorizedException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new UnauthorizedException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("INVALID_CREDENTIALS", e);
        }
    }

    public UserDTO registerUser(UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            throw new BadRequestException("User already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userDTO.getPassword());

        System.out.println("Password before encoding: " + userDTO.getPassword());
        System.out.println("Password after encoding: " + encodedPassword);
//        System.out.println("BCryptPasswordEncoder: " + bCryptPasswordEncoder);


        User newUser = new User(
                userDTO.getEmail(),
                encodedPassword,
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getDateOfBirth()
        );

        User registeredUser = userRepository.save(newUser);
        System.out.println("Encoded password: " + encodedPassword);
        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails, registeredUser.getId());


        UserDTO mappedUserDTO = mapUserToUserDTO(registeredUser);
        mappedUserDTO.setToken(token);

        return mappedUserDTO;
    }

    public User addFavorite(Integer userId, Integer recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);

        UserFavoriteRecipeDTO userFavoriteRecipeDTO = new UserFavoriteRecipeDTO();
        userFavoriteRecipeDTO.setUser(user);
        userFavoriteRecipeDTO.setFavoriteRecipes(user.getFavoriteRecipes());

        return user;
    }

    public UserFavoriteRecipeDTO getFavoriteRecipes(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserFavoriteRecipeDTO userFavoriteRecipeDTO = new UserFavoriteRecipeDTO();
        userFavoriteRecipeDTO.setUser(user);
        userFavoriteRecipeDTO.setFavoriteRecipes(user.getFavoriteRecipes());
        return userFavoriteRecipeDTO;
    }

    public void removeFavorite(Integer userId, Integer recipeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        user.getFavoriteRecipes().remove(recipe);  // Make sure this line actually removes the recipe
        userRepository.save(user);
    }



    private User getOrphanUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String orphanUsername = "orphanuser@mealify.com";
        User orphanUser = userRepository.findByEmail(orphanUsername);
        if (orphanUser == null) {
            orphanUser = new User();
            orphanUser.setEmail(orphanUsername);
            orphanUser.setPassword(encoder.encode("defaultPassword"));
            userRepository.save(orphanUser);
        }
        return orphanUser;
    }

    public void deleteUser(Integer userId, Boolean deleteRecipes) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        User orphanUser = getOrphanUser();

        if (deleteRecipes) {

            userRepository.deleteById(userId);
        } else {
            List<Recipe> recipes = user.getRecipes();
            for (Recipe recipe : recipes) {
                recipe.setUser(orphanUser);
            }
            recipeRepository.saveAll(user.getRecipes());
            recipeRepository.flush();
            userRepository.deleteById(userId);
        }
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

    public UpdateUserDTO mapUserToUpdateUserDTO(User user) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
//        updateUserDTO.setId(user.getId());
        updateUserDTO.setFirstName(user.getFirstName());
        updateUserDTO.setLastName(user.getLastName());
        updateUserDTO.setPassword(user.getPassword());
        return updateUserDTO;
    }


}