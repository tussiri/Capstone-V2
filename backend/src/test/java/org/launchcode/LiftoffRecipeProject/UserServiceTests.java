package org.launchcode.LiftoffRecipeProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.launchcode.LiftoffRecipeProject.DTO.LoginDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UpdateUserDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserDTO;
import org.launchcode.LiftoffRecipeProject.DTO.UserWithRecipesDTO;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.BadRequestException;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.UnauthorizedException;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.security.JwtTokenUtil;
import org.launchcode.LiftoffRecipeProject.services.CustomUserDetailsService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetUser() {
        // Arrange
        User user = createUser(1, "John", "Doe", "johndoe@example.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.getUser(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johndoe@example.com", result.getEmail());

        verify(userRepository).findById(1);
    }

    @Test
    public void testGetUser_UserNotFound() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(1));

        // Assert
        verify(userRepository).findById(1);
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        Page<User> userPage = createMockUserPage();
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Act
        Page<UserDTO> result = userService.getAllUsers(Pageable.unpaged());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        List<UserDTO> userDTOs = result.getContent();
        assertEquals(1, userDTOs.get(0).getId());
        assertEquals("John", userDTOs.get(0).getFirstName());
        assertEquals("Doe", userDTOs.get(0).getLastName());
        assertEquals("johndoe@example.com", userDTOs.get(0).getEmail());

        assertEquals(2, userDTOs.get(1).getId());
        assertEquals("Jane", userDTOs.get(1).getFirstName());
        assertEquals("Smith", userDTOs.get(1).getLastName());
        assertEquals("janesmith@example.com", userDTOs.get(1).getEmail());

        verify(userRepository).findAll(any(Pageable.class));
    }

    @Test
    public void testGetAllUsersWithRecipes() {
        // Arrange
        Page<User> userPage = createMockUserPage();
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Act
        Page<UserWithRecipesDTO> result = userService.getAllUsersWithRecipes(Pageable.unpaged());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        List<UserWithRecipesDTO> userWithRecipesDTOs = result.getContent();
        assertEquals(1, userWithRecipesDTOs.get(0).getId());
        assertEquals("John", userWithRecipesDTOs.get(0).getFirstName());
        assertEquals("Doe", userWithRecipesDTOs.get(0).getLastName());
        assertEquals("johndoe@example.com", userWithRecipesDTOs.get(0).getEmail());

        assertEquals(2, userWithRecipesDTOs.get(1).getId());
        assertEquals("Jane", userWithRecipesDTOs.get(1).getFirstName());
        assertEquals("Smith", userWithRecipesDTOs.get(1).getLastName());
        assertEquals("janesmith@example.com", userWithRecipesDTOs.get(1).getEmail());

        verify(userRepository).findAll(any(Pageable.class));
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        UpdateUserDTO updatedUserDTO = new UpdateUserDTO();
        updatedUserDTO.setFirstName("UpdatedFirstName");
        updatedUserDTO.setLastName("UpdatedLastName");
        updatedUserDTO.setPassword("NewPassword");
        User existingUser = createUser(1, "John", "Doe", "johndoe@example.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        UpdateUserDTO result = userService.updateUser(1, updatedUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());
        assertEquals("NewPassword", result.getPassword());

        verify(userRepository).findById(1);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("UpdatedFirstName", capturedUser.getFirstName());
        assertEquals("UpdatedLastName", capturedUser.getLastName());
        assertTrue(bCryptPasswordEncoder.matches("NewPassword", capturedUser.getPassword()));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Arrange
        UpdateUserDTO updatedUserDTO = new UpdateUserDTO();
        updatedUserDTO.setFirstName("UpdatedFirstName");
        updatedUserDTO.setLastName("UpdatedLastName");
        updatedUserDTO.setPassword("NewPassword");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1, updatedUserDTO));

        verify(userRepository).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLoginUser_ValidCredentials() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("johndoe@example.com");
        loginDTO.setPassword("password");

        User existingUser = createUser(1, "John", "Doe", "johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(existingUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername("johndoe@example.com")).thenReturn(mock(UserDetails.class));
        when(jwtTokenUtil.generateToken(any(UserDetails.class), eq(1))).thenReturn("jwtToken");

        // Act
        UserDTO result = userService.loginUser(loginDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johndoe@example.com", result.getEmail());
        assertEquals("jwtToken", result.getToken());

        verify(userRepository).findByEmail("johndoe@example.com");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("johndoe@example.com");
        verify(jwtTokenUtil).generateToken(any(UserDetails.class), eq(1));
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("johndoe@example.com");
        loginDTO.setPassword("wrongpassword");

        User existingUser = createUser(1, "John", "Doe", "johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(existingUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> userService.loginUser(loginDTO));

        verify(userRepository).findByEmail("johndoe@example.com");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtTokenUtil, never()).generateToken(any(UserDetails.class), anyInt());
    }

    @Test
    public void testRegisterUser() {
        // Arrange
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setFirstName("New");
        newUserDTO.setLastName("User");
        newUserDTO.setEmail("newuser@example.com");
        newUserDTO.setPassword("password");
        newUserDTO.setDateOfBirth(LocalDate.now());

        User newUser = createUser(1, "New", "User", "newuser@example.com");
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userDetailsService.loadUserByUsername("newuser@example.com")).thenReturn(mock(UserDetails.class));
        when(jwtTokenUtil.generateToken(any(UserDetails.class), eq(1))).thenReturn("jwtToken");

        // Act
        UserDTO result = userService.registerUser(newUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals("jwtToken", result.getToken());

        verify(userRepository).findByEmail("newuser@example.com");
        verify(userRepository).save(userCaptor.capture());
        verify(userDetailsService).loadUserByUsername("newuser@example.com");
        verify(jwtTokenUtil).generateToken(any(UserDetails.class), eq(1));

        User capturedUser = userCaptor.getValue();
        assertEquals("New", capturedUser.getFirstName());
        assertEquals("User", capturedUser.getLastName());
        assertEquals("newuser@example.com", capturedUser.getEmail());
        System.out.println("Matches: " + bCryptPasswordEncoder.matches("password", capturedUser.getPassword()));
        assertTrue(bCryptPasswordEncoder.matches("password", capturedUser.getPassword()));
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        // Arrange
        UserDTO userDTO = createUserDTO("John", "Doe", "johndoe@example.com", "password");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(createUser(1, "John", "Doe", "johndoe@example.com"));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.registerUser(userDTO));

        verify(userRepository).findByEmail("johndoe@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtTokenUtil, never()).generateToken(any(UserDetails.class), anyInt());
    }

    @Test
    public void testAddFavorite() {
        // Arrange
        User user = createUser(1, "John", "Doe", "johndoe@example.com");
        Recipe recipe = createRecipe(1, "Recipe 1");
        user.getFavoriteRecipes().add(recipe.getId());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.addFavorite(1, 2);

        // Assert
        assertEquals(2, user.getFavoriteRecipes().size());
        assertTrue(user.getFavoriteRecipes().contains(1));
        assertTrue(user.getFavoriteRecipes().contains(2));

        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }

    @Test
    public void testAddFavorite_UserNotFound() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.addFavorite(1, 2));

        verify(userRepository).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRemoveFavorite() {
        // Arrange
        User user = createUser(1, "John", "Doe", "johndoe@example.com");
        Recipe recipe1 = createRecipe(1, "Recipe 1");
        Recipe recipe2 = createRecipe(2, "Recipe 2");
        user.getFavoriteRecipes().add(recipe1.getId());
        user.getFavoriteRecipes().add(recipe2.getId());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.removeFavorite(1, 1);

        // Assert
        assertEquals(1, user.getFavoriteRecipes().size());
        assertFalse(user.getFavoriteRecipes().contains(1));
        assertTrue(user.getFavoriteRecipes().contains(2));

        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }

    @Test
    public void testRemoveFavorite_UserNotFound() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.removeFavorite(1, 1));

        verify(userRepository).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }

    private User createUser(int id, String firstName, String lastName, String email) {

        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        bCryptPasswordEncoder.encode(user.getPassword());
        user.setEmail(email);
        return user;
    }

    private Page<User> createMockUserPage() {
        List<User> users = new ArrayList<>();
        users.add(createUser(1, "John", "Doe", "johndoe@example.com"));
        users.add(createUser(2, "Jane", "Smith", "janesmith@example.com"));
        return new PageImpl<>(users);
    }

    private UserDTO createUserDTO(String firstName, String lastName, String email, String password) {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        bCryptPasswordEncoder.encode(userDTO.getPassword());
        return userDTO;
    }

    private Recipe createRecipe(int id, String name) {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        return recipe;
    }
}
