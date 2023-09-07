package org.launchcode.LiftoffRecipeProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.launchcode.LiftoffRecipeProject.DTO.*;
import org.launchcode.LiftoffRecipeProject.controllers.UserController;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Setup
        UserDTO user1 = new UserDTO();
        UserDTO user2 = new UserDTO();
        List<UserDTO> userList = Arrays.asList(user1, user2);
        Page<UserDTO> userPage = new PageImpl<>(userList);
        // Mock
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userPage);
        // Execute
        ResponseEntity<ResponseWrapper<Page<UserDTO>>> response = userController.getAllUsers(Pageable.unpaged());
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userPage, response.getBody().getData());
    }

    @Test
    public void testGetAllUsersWithRecipes() {
        UserWithRecipesDTO user1 = new UserWithRecipesDTO();
        UserWithRecipesDTO user2 = new UserWithRecipesDTO();
        List<UserWithRecipesDTO> userList = Arrays.asList(user1, user2);
        Page<UserWithRecipesDTO> userPage = new PageImpl<>(userList);

        when(userService.getAllUsersWithRecipes(any(Pageable.class))).thenReturn(userPage);

        ResponseEntity<ResponseWrapper<Page<UserWithRecipesDTO>>> response = userController.getAllUsersWithRecipes(Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userPage, response.getBody().getData());
    }

    @Test
    public void testUpdateUser() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        when(userService.updateUser(any(Integer.class), any(UpdateUserDTO.class))).thenReturn(updateUserDTO);

        ResponseEntity<ResponseWrapper<UpdateUserDTO>> response = userController.updateUser(1, new UpdateUserDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateUserDTO, response.getBody().getData());
    }

    @Test
    public void testDeleteUser_HappyPath() {
        // Setup
        doNothing().when(userService).deleteUser(any(Integer.class), any(Boolean.class));

        // Execute
        ResponseEntity<?> response = userController.deleteUser(1, true);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteUser_ExceptionThrown() {
        // Setup
        doThrow(new RuntimeException()).when(userService).deleteUser(any(Integer.class), any(Boolean.class));

        // Execute
        ResponseEntity<?> response = userController.deleteUser(1, true);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateAccountInfo() {
        // Setup
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        when(userService.updateUser(any(Integer.class), any(UpdateUserDTO.class))).thenReturn(updateUserDTO);

        // Execute
        ResponseEntity<ResponseWrapper<UpdateUserDTO>> response = userController.updateAccountInfo(1, new UpdateUserDTO());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateUserDTO, response.getBody().getData());
    }

    @Test
    public void testAddFavorite() {
        // Setup
        UserFavoriteRecipeDTO userFavoriteRecipeDTO = new UserFavoriteRecipeDTO();
        when(userService.getFavoriteRecipes(any(Integer.class))).thenReturn(userFavoriteRecipeDTO);

        // Execute
        ResponseEntity<ResponseWrapper<UserFavoriteRecipeDTO>> response = userController.addFavorite(1, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userFavoriteRecipeDTO, response.getBody().getData());
    }

    @Test
    public void testRemoveFavorite() {
        // Setup
        UserFavoriteRecipeDTO userFavoriteRecipeDTO = new UserFavoriteRecipeDTO();
        when(userService.getFavoriteRecipes(any(Integer.class))).thenReturn(userFavoriteRecipeDTO);

        // Execute
        ResponseEntity<ResponseWrapper<UserFavoriteRecipeDTO>> response = userController.removeFavorite(1, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userFavoriteRecipeDTO, response.getBody().getData());
    }


}
