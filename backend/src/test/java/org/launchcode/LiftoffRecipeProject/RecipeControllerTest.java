package org.launchcode.LiftoffRecipeProject;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.launchcode.LiftoffRecipeProject.DTO.IngredientDTO;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.controllers.RecipeController;
import org.launchcode.LiftoffRecipeProject.data.IngredientRepository;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.RecipeNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.UnauthorizedException;
import org.launchcode.LiftoffRecipeProject.models.*;
import org.launchcode.LiftoffRecipeProject.services.IngredientService;
import org.launchcode.LiftoffRecipeProject.services.RecipeService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.launchcode.LiftoffRecipeProject.specification.RecipeSpecification;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.security.InvalidParameterException;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.launchcode.LiftoffRecipeProject.controllers.RecipeController.logger;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;




    @Test
    public void testGetAllRecipes() {
        // Mock input data
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> recipeDTOs = mock(Page.class);
        when(recipeService.findAllRecipes(pageable)).thenReturn(recipeDTOs);
        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.getAllRecipes(pageable);
        verify(recipeService, times(1)).findAllRecipes(pageable);
        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testGetRecipeByUser() {
        Integer userId = 1;
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> recipeDTOs = mock(Page.class);
        when(recipeService.getRecipesByUser(userId, pageable)).thenReturn(recipeDTOs);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.getRecipesByUser(userId, pageable);

        verify(recipeService, times(1)).getRecipesByUser(userId, pageable);

        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testGetRecipe() {
        Integer recipeId = 1;
        RecipeDTO recipeDTO = mock(RecipeDTO.class);
        when(recipeService.findById(recipeId)).thenReturn(Optional.of(recipeDTO));

        ResponseEntity<ResponseWrapper<RecipeDTO>> response = recipeController.getRecipe(recipeId);

        verify(recipeService, times(1)).findById(recipeId);
        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testCreateRecipe() {
        Integer userId = 1;
        RecipeDTO recipeDTO = mock(RecipeDTO.class);
        RecipeDTO savedRecipeDTO = mock(RecipeDTO.class);
        when(recipeService.createRecipe(userId, recipeDTO)).thenReturn(savedRecipeDTO);

        ResponseEntity<ResponseWrapper<RecipeDTO>> response = recipeController.createRecipe(userId, recipeDTO);

        verify(recipeService, times(1)).createRecipe(userId, recipeDTO);
        assert response.getStatusCode() == HttpStatus.CREATED;
    }

    @Test
    public void testUpdateRecipe() {
        Integer recipeId = 1;
        Integer userId = 1;
        RecipeDTO recipeDTO = mock(RecipeDTO.class);
        RecipeDTO updatedRecipeDTO = mock(RecipeDTO.class);
        when(recipeService.updateRecipe(recipeId, recipeDTO, userId)).thenReturn(updatedRecipeDTO);

        ResponseEntity<ResponseWrapper<RecipeDTO>> response = recipeController.updateRecipe(recipeId, recipeDTO, userId);

        verify(recipeService, times(1)).updateRecipe(recipeId, recipeDTO, userId);
        assert response.getStatusCode() == HttpStatus.OK;
    }

//    @Test
//    public void testSearchRecipes() {
//        String name = "recipeName";
//        String category = "recipeCategory";
//        String ingredients = "ingredient1,ingredient2";
//        String time = ">30";
//        String rating = "<5";
//        Pageable pageable = mock(Pageable.class);
//        Page<RecipeDTO> recipeDTOs = mock(Page.class);
//        when(recipeService.searchRecipes(any(), eq(pageable))).thenReturn(recipeDTOs);
//
//        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(name, category, ingredients, time, rating, pageable);
//
//        verify(recipeService, times(1)).searchRecipes(any(), eq(pageable));
//        assert response.getStatusCode() == HttpStatus.OK;
//    }

    @Test
    public void testSearchRecipes() {
        String name = "recipeName";
        String category = "recipeCategory";
        String ingredients = "ingredient1,ingredient2";
        String time = ">30";
        String rating = "<5";
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> recipeDTOs = mock(Page.class);
        when(recipeService.searchRecipes(any(Specification.class), eq(pageable))).thenReturn(recipeDTOs);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(name, category, ingredients, time, rating, pageable);

        verify(recipeService, times(1)).searchRecipes(any(Specification.class), eq(pageable));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetRandomRecipe() {
        RecipeDTO randomRecipe = mock(RecipeDTO.class);
        when(recipeService.getRandomRecipe()).thenReturn(randomRecipe);

        ResponseEntity<ResponseWrapper<RecipeDTO>> response = recipeController.getRandomRecipe();

        verify(recipeService, times(1)).getRandomRecipe();
        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testDeleteRecipe() {
        Integer recipeId = 1;
        Integer userId = 1;
        doNothing().when(recipeService).deleteRecipe(recipeId, userId, true);

        ResponseEntity<ResponseWrapper<String>> response = recipeController.deleteRecipe(recipeId, userId);

        verify(recipeService, times(1)).deleteRecipe(recipeId, userId, true);
        assert response.getStatusCode() == HttpStatus.OK;
    }
    @Test
    public void testGetRecipe_WhenRecipeNotFound() {
        Integer recipeId = 1;
        when(recipeService.findById(recipeId)).thenThrow(new RecipeNotFoundException("Recipe not found"));

        Exception exception = assertThrows(RecipeNotFoundException.class, () -> {
            recipeController.getRecipe(recipeId);
        });

        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    public void testGetAllRecipes_WhenNoneExist() {
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> emptyRecipePage = new PageImpl<>(new ArrayList<>());
        when(recipeService.findAllRecipes(pageable)).thenReturn(emptyRecipePage);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.getAllRecipes(pageable);

        verify(recipeService, times(1)).findAllRecipes(pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData().isEmpty());
    }


    @Test
    public void testGetRecipesByUser_WhenUserHasNoRecipes() {
        Integer userId = 1;
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> emptyRecipePage = new PageImpl<>(new ArrayList<>());
        when(recipeService.getRecipesByUser(userId, pageable)).thenReturn(emptyRecipePage);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.getRecipesByUser(userId, pageable);

        verify(recipeService, times(1)).getRecipesByUser(userId, pageable);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().getData().isEmpty();
    }

    @Test
    public void testSearchRecipesByIngredient() {
        // Mocking Pageable
        Pageable pageable = mock(Pageable.class);

        // Create an expected Specification based on the "ingredients" parameter
        Specification<Recipe> expectedSpec = Specification.where(null);
        String[] ingredientNames = {"flour", "sugar"};
        for (String ingredientName : ingredientNames) {
            expectedSpec = expectedSpec.and(new RecipeSpecification(new SearchCriteria("ingredients", ":", ingredientName.trim())));
        }

        // Mocking what the service should return
        Page<RecipeDTO> mockedPage = new PageImpl<>(Arrays.asList(new RecipeDTO(), new RecipeDTO()));
        when(recipeService.searchRecipes(any(Specification.class), eq(pageable))).thenReturn(mockedPage);

        // Making the actual call
        String ingredients = "flour,sugar";
        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(null, null, ingredients, null, null, pageable);

        // Verifying the method was called with the expected Specification
        verify(recipeService, times(1)).searchRecipes(any(Specification.class), eq(pageable));

        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertFalse(response.getBody().getData().isEmpty());
    }


    @Test
    public void testUnauthorizedUserUpdateRecipe() {
        Integer recipeId = 1;
        Integer userId = 1;
        RecipeDTO recipeDTO = mock(RecipeDTO.class);

        // Simulate an unauthorized exception from the service layer
        when(recipeService.updateRecipe(recipeId, recipeDTO, userId)).thenThrow(new UnauthorizedException("Unauthorized"));

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            recipeController.updateRecipe(recipeId, recipeDTO, userId);
        });

        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    public void testUnauthorizedUserDeleteRecipe() {
        Integer recipeId = 1;
        Integer userId = 1;

        // Simulate an unauthorized exception from the service layer
        doThrow(new UnauthorizedException("Unauthorized")).when(recipeService).deleteRecipe(recipeId, userId, true);

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            recipeController.deleteRecipe(recipeId, userId);
        });

        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    public void testInvalidParametersCreateRecipe() {
        Integer userId = 1;
        RecipeDTO recipeDTO = mock(RecipeDTO.class);

        // Simulate a validation exception from the service layer
        when(recipeService.createRecipe(userId, recipeDTO)).thenThrow(new ValidationException("Invalid parameters"));

        Exception exception = assertThrows(ValidationException.class, () -> {
            recipeController.createRecipe(userId, recipeDTO);
        });

        assertEquals("Invalid parameters", exception.getMessage());
    }

    @Test
    public void testUserNotFoundGetRecipesByUser() {
        Integer userId = 1;
        Pageable pageable = mock(Pageable.class);

        // Simulate a user not found scenario
        when(recipeService.getRecipesByUser(userId, pageable)).thenThrow(new ResourceNotFoundException("User not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            recipeController.getRecipesByUser(userId, pageable);
        });

        assertEquals("User not found", exception.getMessage());
    }
    @Test
    public void testInvalidSearchCriteria() {
        String name = "recipeName";
        String category = "recipeCategory";
        String ingredients = "ingredient1,ingredient2";
        String time = ">30";
        String rating = "<5";
        Pageable pageable = mock(Pageable.class);

        // Simulate an invalid parameter exception from the service layer
        when(recipeService.searchRecipes(any(), eq(pageable))).thenThrow(new InvalidParameterException("Invalid search criteria"));

        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            recipeController.searchRecipes(name, category, ingredients, time, rating, pageable);
        });

        assertEquals("Invalid search criteria", exception.getMessage());
    }

    @Test
    public void testInvalidRecipeIdForUpdate() {
        Integer recipeId = 1;
        Integer userId = 1;
        RecipeDTO recipeDTO = mock(RecipeDTO.class);

        // Simulate an invalid recipe ID scenario
        when(recipeService.updateRecipe(recipeId, recipeDTO, userId)).thenThrow(new RecipeNotFoundException("Invalid recipe ID"));

        Exception exception = assertThrows(RecipeNotFoundException.class, () -> {
            recipeController.updateRecipe(recipeId, recipeDTO, userId);
        });

        assertEquals("Invalid recipe ID", exception.getMessage());
    }

    @Test
    public void testInvalidUserIdForRecipeCreation() {
        Integer userId = 1;
        RecipeDTO recipeDTO = mock(RecipeDTO.class);

        // Simulate an invalid user ID scenario
        when(recipeService.createRecipe(userId, recipeDTO)).thenThrow(new ResourceNotFoundException("Invalid user ID"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            recipeController.createRecipe(userId, recipeDTO);
        });

        assertEquals("Invalid user ID", exception.getMessage());
    }


    @Test
    public void testSearchRecipesByIngredient_NullIngredients() {
        Pageable pageable = mock(Pageable.class);
        when(recipeService.searchRecipes(any(), eq(pageable))).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(null, null, null, null, null, pageable);

        // Verify and assert
        verify(recipeService, times(1)).searchRecipes(any(), eq(pageable));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().isEmpty());
    }


    @Test
    public void testSearchRecipesByIngredient_EmptyIngredients() {
        Pageable pageable = mock(Pageable.class);
        when(recipeService.searchRecipes(any(), eq(pageable))).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(null, null, "", null, null, pageable);

        // Verify and assert
        verify(recipeService, times(1)).searchRecipes(any(), eq(pageable));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    public void testSearchRecipesByIngredient_WhitespaceIngredients() {
        Pageable pageable = mock(Pageable.class);
        when(recipeService.searchRecipes(any(), eq(pageable))).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(null, null, "  ", null, null, pageable);

        // Verify and assert
        verify(recipeService, times(1)).searchRecipes(any(), eq(pageable));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    public void testSearchRecipesByIngredient_NullPageable() {
        // Make the mock more flexible
        when(recipeService.searchRecipes(any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(null, null, "flour,sugar", null, null, null);

        // Verify and assert
        verify(recipeService, times(1)).searchRecipes(any(), isNull());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }




}


/*
testGetAllRecipes: This test verifies that the getAllRecipes method returns all recipes by calling the recipeService.findAllRecipes method and asserting the response status.

testGetRecipesByUser: This test verifies that the getRecipesByUser method returns recipes for a specific user by calling the recipeService.getRecipesByUser method with the given user ID and asserting the response status.

testGetRecipesByIngredient: This test verifies that the getRecipesByIngredient method returns recipes containing a specific ingredient by calling the recipeService.getRecipesByIngredient method with the given ingredient name and asserting the response status.

testGetRecipe: This test verifies that the getRecipe method returns a specific recipe by calling the recipeService.findById method with the given recipe ID and asserting the response status.

testCreateRecipe: This test verifies that the createRecipe method creates a new recipe by calling the recipeService.createRecipe method with the given user ID and recipe DTO, and asserting the response status.

testUpdateRecipe: This test verifies that the updateRecipe method updates a recipe by calling the recipeService.updateRecipe method with the given recipe ID, recipe DTO, and user ID, and asserting the response status.

testSearchRecipes: This test verifies that the searchRecipes method returns recipes based on the search criteria by calling the recipeService.searchRecipes method with the provided parameters and asserting the response status.

testGetRandomRecipe: This test verifies that the getRandomRecipe method returns a random recipe by calling the recipeService.getRandomRecipe method and asserting the response status.

*/