package org.launchcode.LiftoffRecipeProject;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.controllers.RecipeController;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.UnauthorizedException;
import org.launchcode.LiftoffRecipeProject.services.IngredientService;
import org.launchcode.LiftoffRecipeProject.services.RecipeService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void testGetRecipeByUser(){
        Integer userId = 1;
        Pageable pageable=mock(Pageable.class);
        Page<RecipeDTO> recipeDTOs = mock(Page.class);
        when(recipeService.getRecipesByUser(userId, pageable)).thenReturn(recipeDTOs);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response=recipeController.getRecipesByUser(userId, pageable);

        verify(recipeService, times(1)).getRecipesByUser(userId,pageable);

        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testGetRecipesByIngredient() {

        String ingredientName = "ingredient";
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> recipeDTOs = mock(Page.class);
        when(recipeService.getRecipesByIngredient(ingredientName, pageable)).thenReturn(recipeDTOs);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.getRecipesByIngredient(ingredientName, pageable);

        verify(recipeService, times(1)).getRecipesByIngredient(ingredientName, pageable);
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

    @Test
    public void testSearchRecipes() {
        String name = "recipeName";
        String category = "recipeCategory";
        String ingredients = "ingredient1,ingredient2";
        String time = ">30";
        String rating = "<5";
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> recipeDTOs = mock(Page.class);
        when(recipeService.searchRecipes(any(), eq(pageable))).thenReturn(recipeDTOs);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.searchRecipes(name, category, ingredients, time, rating, pageable);

        verify(recipeService, times(1)).searchRecipes(any(), eq(pageable));
        assert response.getStatusCode() == HttpStatus.OK;
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
    public void testGetAllRecipes_WhenNoneExist() {
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> emptyRecipePage = new PageImpl<>(new ArrayList<>());
        when(recipeService.findAllRecipes(pageable)).thenReturn(emptyRecipePage);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.getAllRecipes(pageable);

        verify(recipeService, times(1)).findAllRecipes(pageable);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().getData().isEmpty();
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
    public void testGetRecipesByIngredient_WhenNoRecipesContainIngredient() {

        String ingredientName = "ingredient";
        Pageable pageable = mock(Pageable.class);
        Page<RecipeDTO> emptyRecipePage = new PageImpl<>(new ArrayList<>());
        when(recipeService.getRecipesByIngredient(ingredientName, pageable)).thenReturn(emptyRecipePage);

        ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> response = recipeController.getRecipesByIngredient(ingredientName, pageable);

        verify(recipeService, times(1)).getRecipesByIngredient(ingredientName, pageable);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().getData().isEmpty();
    }

    @Test
    public void testGetRecipe_WhenRecipeNotFound() {
        Integer recipeId = 1;
        when(recipeService.findById(recipeId)).thenReturn(Optional.empty());

        ResponseEntity<ResponseWrapper<RecipeDTO>> response = recipeController.getRecipe(recipeId);

        verify(recipeService, times(1)).findById(recipeId);
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
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