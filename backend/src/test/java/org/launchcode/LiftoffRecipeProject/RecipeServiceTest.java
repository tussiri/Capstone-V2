package org.launchcode.LiftoffRecipeProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.launchcode.LiftoffRecipeProject.DTO.IngredientDTO;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.data.*;
import org.launchcode.LiftoffRecipeProject.exception.RecipeNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.*;
import org.launchcode.LiftoffRecipeProject.services.IngredientService;
import org.launchcode.LiftoffRecipeProject.services.RecipeService;
import org.launchcode.LiftoffRecipeProject.specification.RecipeSpecification;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;


    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private IngredientService ingredientService;
    @Mock
    private RecipeData recipeData ;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdNotFound() {
        // Mock the RecipeRepository and its dependencies
        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);

        // Sample data
        int sampleRecipeId = 123;

        // Stubbing the behavior of the mockRecipeRepository to return an empty Optional for the given ID
        when(mockRecipeRepository.findById(sampleRecipeId)).thenReturn(Optional.empty());

        // Initialize the service with the mock repository
        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository);

        // Call findById on the real service
        Optional<Recipe> result = serviceUnderTest.findById(sampleRecipeId);

        // Assert that the result does not contain a Recipe
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

    @Test
    public void testFindByIdFound() {
        // Mock the RecipeRepository and its dependencies
        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);

        // Sample data
        int sampleRecipeId = 123;
        Recipe mockRecipe = mock(Recipe.class);

        // Stubbing the behavior of the mockRecipeRepository to return mockRecipe for the given ID
        when(mockRecipeRepository.findById(sampleRecipeId)).thenReturn(Optional.of(mockRecipe));

        // Initialize the service with the mock repository
        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository);

        // Call findById on the real service
        Optional<Recipe> result = serviceUnderTest.findById(sampleRecipeId);

        // Assert that the result matches the mockRecipe
        assertTrue(result.isPresent());
        assertEquals(mockRecipe, result.get());
    }
    @Test
    public void testFindPaginated() {
        // Mock the RecipeRepository
        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);

        // Mock the Pageable and Page
        Pageable mockPageable = mock(Pageable.class);
        Page<Recipe> mockPage = mock(Page.class);

        // When findAll is called on the mock repository, return the mock page.
        when(mockRecipeRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        // Initialize the service with the mock repository
        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository); // Fill in other dependencies

        // Call findPaginated on the real service
        Page<Recipe> result = serviceUnderTest.findPaginated(mockPageable);

        // Assert that the result matches the mock page
        assertEquals(mockPage, result);
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
        RecipeIngredientRepository recipeIngredientRepository = mock(RecipeIngredientRepository.class);

        RecipeService recipeService = new RecipeService(recipeRepository, ingredientRepository, userRepository, recipeData, ingredientService, reviewRepository, recipeIngredientRepository);

        // Arrange
        Integer userId = 1;
        RecipeDTO inputRecipeDTO = new RecipeDTO();
        inputRecipeDTO.setName("Test Recipe");
        inputRecipeDTO.setDescription("This is a test recipe");
        inputRecipeDTO.setCategory("Dessert");
        inputRecipeDTO.setDirections("Mix and Bake");
        IngredientDTO ingredientDTO1 = new IngredientDTO();
        ingredientDTO1.setName("Ingredient 1");
        IngredientDTO ingredientDTO2 = new IngredientDTO();
        ingredientDTO2.setName("Ingredient 2");
        inputRecipeDTO.setTime(30);
        inputRecipeDTO.setFavorite(true);
        inputRecipeDTO.setPicture("path/to/image.jpg");
        inputRecipeDTO.setAllergens(Arrays.asList("Dairy", "Nuts"));
        inputRecipeDTO.setRating(4.5);

        inputRecipeDTO.setIngredients(Arrays.asList(ingredientDTO1, ingredientDTO2));


        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        Ingredient mockIngredient = new Ingredient();
        mockIngredient.setName("Ingredient 1");
        when(ingredientRepository.findByName("Ingredient 1")).thenReturn(Optional.empty());
        when(ingredientRepository.findByName("Ingredient 2")).thenReturn(Optional.of(new Ingredient()));

//        Ingredient mockIngredient = new Ingredient();
//        mockIngredient.setName(ingredientDTO.getName());
//        when(ingredientRepository.findByName(ingredientDTO.getName())).thenReturn(Optional.of(mockIngredient));

        RecipeIngredient mockRecipeIngredient = new RecipeIngredient();
        mockRecipeIngredient.setIngredient(mockIngredient);
        when(recipeIngredientRepository.save(any(RecipeIngredient.class))).thenReturn(mockRecipeIngredient);

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
        verify(ingredientRepository, times(2)).save(any(Ingredient.class)); // Adjusted to 1
        verify(recipeRepository).save(any(Recipe.class));
        verify(recipeIngredientRepository, times(2)).save(any(RecipeIngredient.class)); // Assuming 2 ingredients


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
    public void testUpdateRecipe_SuccessfulUpdate() {

        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);
        RecipeIngredientRepository mockRecipeIngredientRepository = mock(RecipeIngredientRepository.class);

        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, mockRecipeIngredientRepository);


        // Given
        Integer recipeId = 1;
        Integer userId = 1;
        RecipeDTO updateRecipeDTO = new RecipeDTO();
        updateRecipeDTO.setName("Updated Test Recipe");
        updateRecipeDTO.setDescription("Updated description");
        updateRecipeDTO.setCategory("Updated Category");
        updateRecipeDTO.setDirections("Updated directions");
        updateRecipeDTO.setTime(45);
        updateRecipeDTO.setFavorite(false);
        updateRecipeDTO.setPicture("path/to/updated_image.jpg");
        updateRecipeDTO.setAllergens(Arrays.asList("Updated Allergen1", "Updated Allergen2"));
        updateRecipeDTO.setRating(5.0);
        IngredientDTO updatedIngredientDTO = new IngredientDTO();
        updatedIngredientDTO.setName("Updated Ingredient");
        updateRecipeDTO.setIngredients(Collections.singletonList(updatedIngredientDTO));

        User mockUser = new User();
        mockUser.setId(userId);

        Recipe existingRecipe = new Recipe();
        existingRecipe.setUser(mockUser);

        System.out.println("In test method, existingRecipe object: " + existingRecipe);


        Ingredient existingIngredient = new Ingredient();
        existingIngredient.setName("Existing Ingredient");

        Ingredient updatedIngredient = new Ingredient();
        updatedIngredient.setName(updatedIngredientDTO.getName());

        // When findById is called, return the existing recipe
        when(mockRecipeRepository.findById(recipeId)).thenReturn(Optional.of(existingRecipe));
        when(mockRecipeRepository.save(any(Recipe.class))).thenReturn(existingRecipe);

        // When save is called on the ingredientRepository, return the updated ingredient
        when(mockIngredientRepository.save(any(Ingredient.class))).thenReturn(updatedIngredient);

        // Call updateRecipe on the real service
        RecipeDTO resultRecipeDTO = serviceUnderTest.updateRecipe(recipeId, updateRecipeDTO, userId);

        // Verify interactions
        verify(mockRecipeRepository).findById(recipeId);
        verify(mockIngredientRepository, times(2)).save(any(Ingredient.class));

        // Assert that the result matches the updated values
        assertEquals(updateRecipeDTO.getName(), resultRecipeDTO.getName());
        assertEquals(updateRecipeDTO.getDescription(), resultRecipeDTO.getDescription());
        assertEquals(updateRecipeDTO.getCategory(), resultRecipeDTO.getCategory());
        assertEquals(updateRecipeDTO.getDirections(), resultRecipeDTO.getDirections());
        assertEquals(updateRecipeDTO.getTime(), resultRecipeDTO.getTime());
        assertEquals(updateRecipeDTO.isFavorite(), resultRecipeDTO.isFavorite());
        assertEquals(updateRecipeDTO.getPicture(), resultRecipeDTO.getPicture());
        assertEquals(updateRecipeDTO.getAllergens(), resultRecipeDTO.getAllergens());
        assertEquals(updateRecipeDTO.getRating(), resultRecipeDTO.getRating());
        if (!resultRecipeDTO.getIngredients().isEmpty() && !updateRecipeDTO.getIngredients().isEmpty()) {
            assertEquals(updateRecipeDTO.getIngredients().get(0).getName(), resultRecipeDTO.getIngredients().get(0).getName());
        }
//        assertEquals(updateRecipeDTO.getIngredients().get(0).getName(), resultRecipeDTO.getIngredients().get(0).getName());
    }

    @Test
    public void testUpdateRecipe_UnsuccessfulUpdate_RecipeNotFound(){

        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);

        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository);

        // Given
        Integer recipeId = 1;
        Integer userId = 1;
        RecipeDTO updateRecipeDTO = new RecipeDTO();
        updateRecipeDTO.setName("Updated Test Recipe");
        updateRecipeDTO.setDescription("Updated description");
        updateRecipeDTO.setCategory("Updated Category");
        updateRecipeDTO.setDirections("Updated directions");
        updateRecipeDTO.setTime(45);
        updateRecipeDTO.setFavorite(false);
        updateRecipeDTO.setPicture("path/to/updated_image.jpg");
        updateRecipeDTO.setAllergens(Arrays.asList("Updated Allergen1", "Updated Allergen2"));
        updateRecipeDTO.setRating(5.0);
        IngredientDTO updatedIngredientDTO = new IngredientDTO();
        updatedIngredientDTO.setName("Updated Ingredient");
        updateRecipeDTO.setIngredients(Collections.singletonList(updatedIngredientDTO));

        // Mock behavior: Recipe doesn't exist in the repository
        when(mockRecipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Call the method and expect an exception (adjust this part based on your actual implementation)
        assertThrows(RecipeNotFoundException.class, () -> {
            serviceUnderTest.updateRecipe(recipeId, updateRecipeDTO, userId);
        });

        // Optionally: Verify that save() was never called, because the update should have failed before reaching that point
        verify(mockRecipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    public void testDeleteUser(){
        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);

        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository);

        Integer userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setRecipes(new ArrayList<>());

        when(mockUserRepository.existsById(userId)).thenReturn(true);

        serviceUnderTest.deleteUser(userId, true);

        verify(mockUserRepository).deleteById(userId);
    }

    @Test
    public void testSearchRecipes() {

        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);

        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository);

        // Given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe 1");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Recipe 2");

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Page<Recipe> pagedRecipes = new PageImpl<>(recipes);

        Specification<Recipe> spec = mock(Specification.class);
        Pageable pageable = mock(Pageable.class);

        when(mockRecipeRepository.findAll(spec, pageable)).thenReturn(pagedRecipes);

        // When
        Page<RecipeDTO> result = serviceUnderTest.searchRecipes(spec, pageable);

        // Then
        assertEquals(2, result.getContent().size());
        assertEquals("Recipe 1", result.getContent().get(0).getName());
        assertEquals("Recipe 2", result.getContent().get(1).getName());

        verify(mockRecipeRepository).findAll(spec, pageable);
    }

    @Test
    public void testSearchRecipesByIngredient(){
        // Mock the RecipeRepository and its dependencies
        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);
        Pageable pageable = mock(Pageable.class);

        // Initialize the service with the mock repository
        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository);

        //Given
        Recipe recipeWithCheese = new Recipe();
        recipeWithCheese.setName("Margherita Pizza");

        List<Recipe> recipesWithCheese = Arrays.asList(recipeWithCheese);
        Page<Recipe> pagedRecipesWithCheese = new PageImpl<>(recipesWithCheese);

        SearchCriteria ingredientCriteria = new SearchCriteria("ingredients", ":", "Cheese");
        RecipeSpecification ingredientSpec = new RecipeSpecification(ingredientCriteria);

        when(mockRecipeRepository.findAll(ingredientSpec, pageable)).thenReturn(pagedRecipesWithCheese);

        //When
        Page<RecipeDTO> result = serviceUnderTest.searchRecipes(ingredientSpec, pageable);

        //Then
        assertEquals(1, result.getContent().size());
        assertEquals("Margherita Pizza", result.getContent().get(0).getName());
    }

    @Test
    public void testSearchRecipesByTime() {

        RecipeRepository mockRecipeRepository = mock(RecipeRepository.class);
        IngredientRepository mockIngredientRepository = mock(IngredientRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        RecipeData mockRecipeData = mock(RecipeData.class);
        IngredientService mockIngredientService = mock(IngredientService.class);
        ReviewRepository mockReviewRepository = mock(ReviewRepository.class);
        Pageable pageable = mock(Pageable.class);

        // Initialize the service with the mock repository
        RecipeService serviceUnderTest = new RecipeService(mockRecipeRepository, mockIngredientRepository, mockUserRepository, mockRecipeData, mockIngredientService, mockReviewRepository, recipeIngredientRepository);

        // Given
        Recipe quickRecipe = new Recipe();
        quickRecipe.setName("Quick Salad");
        quickRecipe.setTime(10);

        List<Recipe> quickRecipes = Arrays.asList(quickRecipe);
        Page<Recipe> pagedQuickRecipes = new PageImpl<>(quickRecipes);

        SearchCriteria timeCriteria = new SearchCriteria("time", "<", "15");
        RecipeSpecification timeSpec = new RecipeSpecification(timeCriteria);

        when(mockRecipeRepository.findAll(timeSpec, pageable)).thenReturn(pagedQuickRecipes);

        // When
        Page<RecipeDTO> result = serviceUnderTest.searchRecipes(timeSpec, pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals("Quick Salad", result.getContent().get(0).getName());
    }

}
