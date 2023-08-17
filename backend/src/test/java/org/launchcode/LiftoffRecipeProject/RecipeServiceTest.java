package org.launchcode.LiftoffRecipeProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.launchcode.LiftoffRecipeProject.DTO.IngredientDTO;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.data.IngredientRepository;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.RecipeData;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.services.IngredientService;
import org.launchcode.LiftoffRecipeProject.services.RecipeService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        //Arrange
        Integer id = 1;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(mockRecipe));

        //Act
        Optional<RecipeDTO> foundRecipe = recipeService.findById(id);

        //Assert
        assertTrue(foundRecipe.isPresent());
        assertEquals(id, foundRecipe.get().getId());
        verify(recipeRepository, times(1)).findById(id);
    }

    @Test
    public void testCreateRecipe() {
        // Mocking dependencies
        UserRepository userRepository = mock(UserRepository.class);
        IngredientRepository ingredientRepository = mock(IngredientRepository.class);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        RecipeData recipeData = mock(RecipeData.class);
        ReviewRepository reviewRepository = mock(ReviewRepository.class);
        IngredientService ingredientService = mock(IngredientService.class);

        RecipeService recipeService = new RecipeService(recipeRepository, ingredientRepository, userRepository, recipeData, ingredientService, reviewRepository);

        // Arrange
        Integer userId = 1;
        RecipeDTO inputRecipeDTO = new RecipeDTO();
        inputRecipeDTO.setName("Test Recipe");
        inputRecipeDTO.setDescription("This is a test recipe");
        inputRecipeDTO.setCategory("Dessert");
        inputRecipeDTO.setDirections("Mix and Bake");
        inputRecipeDTO.setTime(30);
        inputRecipeDTO.setFavorite(true);
        inputRecipeDTO.setPicture("path/to/image.jpg");
        inputRecipeDTO.setAllergens(Arrays.asList("Dairy", "Nuts"));
        inputRecipeDTO.setRating(4.5);

        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setName("Ingredient 1");
        List<IngredientDTO> ingredientDTOs = Collections.singletonList(ingredientDTO);
        inputRecipeDTO.setIngredients(ingredientDTOs);

        Ingredient mockIngredient = new Ingredient();
        mockIngredient.setName(ingredientDTO.getName());
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(mockIngredient);

        Recipe mockRecipe = new Recipe();
        mockRecipe.setName("Test Recipe");
        mockRecipe.setDescription("This is a test recipe");
        mockRecipe.setCategory("Dessert");
        mockRecipe.setDirections("Mix and Bake");
        mockRecipe.setTime(30);
        mockRecipe.setFavorite(true);
        mockRecipe.setPicture("path/to/image.jpg");
        mockRecipe.setAllergens(Arrays.asList("Dairy", "Nuts"));
        mockRecipe.setRating(4.5);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(mockRecipe);

        RecipeDTO resultRecipeDTO = recipeService.createRecipe(userId, inputRecipeDTO);

        verify(userRepository).findById(userId);
        verify(ingredientRepository).save(any(Ingredient.class));
        verify(recipeRepository).save(any(Recipe.class));

        assertEquals(inputRecipeDTO.getName(), resultRecipeDTO.getName());
        assertEquals(inputRecipeDTO.getDescription(), resultRecipeDTO.getDescription());
        assertEquals(inputRecipeDTO.getCategory(), resultRecipeDTO.getCategory());
        assertEquals(inputRecipeDTO.getDirections(), resultRecipeDTO.getDirections());
        assertEquals(inputRecipeDTO.getTime(), resultRecipeDTO.getTime());
        assertEquals(inputRecipeDTO.isFavorite(), resultRecipeDTO.isFavorite());
        assertEquals(inputRecipeDTO.getPicture(), resultRecipeDTO.getPicture());
        assertEquals(inputRecipeDTO.getAllergens(), resultRecipeDTO.getAllergens());
        assertEquals(inputRecipeDTO.getRating(), resultRecipeDTO.getRating());
    }

    @Test
    public void testFindByIdNotFound() {
        lenient().when(recipeRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<Recipe> result = recipeService.findById(1);

        assertFalse(result.isPresent());
    }


    @Test
    public void testDeleteRecipe_ThrowsExceptionWhenUserNotFound() {
        // Arrange
        Integer userId = 1;
        lenient().when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            recipeService.deleteRecipe(1, userId, true);
        });
    }

//    @Test
//    public void testUpdateRecipe() {
//        // Create mock objects
//        Recipe recipeMock = new Recipe();
//        recipeMock.setId(1);
//        User userMock = new User();
//        userMock.setId(2);
//        recipeMock.setUser(userMock);
//
//        RecipeDTO recipeDtoMock = new RecipeDTO();
//        recipeDtoMock.setName("Updated Recipe");
//
//        // Define mock behaviors
//        when(recipeRepository.findById(1)).thenReturn(Optional.of(recipeMock));
//
//        verify(recipeRepository, times(1)).findById(1);
//
//        // Call the method to test
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//            recipeService.updateRecipe(1, recipeDtoMock, 2);
//        });
//
//        // Assert that the update was successful
//        assertEquals("Recipe not found", exception.getMessage());
//    }


}
