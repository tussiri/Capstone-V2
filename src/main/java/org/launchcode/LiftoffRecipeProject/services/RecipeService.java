package org.launchcode.LiftoffRecipeProject.services;


import org.launchcode.LiftoffRecipeProject.DTO.IngredientDTO;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.data.IngredientRepository;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.UnauthorizedException;
import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.RecipeData;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    private final ReviewRepository reviewRepository;

    private final IngredientService ingredientService;
    private final UserRepository userRepository;
    private final RecipeData recipeData;
    private Ingredient ingredient;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, UserRepository userRepository, RecipeData recipeData, IngredientService ingredientService, ReviewRepository reviewRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.recipeData = recipeData;
        this.ingredientService = ingredientService;
        this.reviewRepository = reviewRepository;
    }


    public RecipeDTO mapToDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        if (recipe.getUser() != null) {
            recipeDTO.setUserId(recipe.getUser().getId());
        }
        recipeDTO.setName(recipe.getName());
        recipeDTO.setDescription(recipe.getDescription());
        recipeDTO.setCategory(recipe.getCategory());
//        recipeDTO.setIngredients(recipe.getIngredients());
        recipeDTO.setDirections(recipe.getDirections());
        recipeDTO.setTime(recipe.getTime());
        recipeDTO.setFavorite(recipe.getFavorite());
        recipeDTO.setPicture(recipe.getPicture());
        recipeDTO.setAllergens(recipe.getAllergens());
//        recipeDTO.setRating(recipe.getRating());

        Double averageRating = reviewRepository.findAverageRatingByRecipeId(recipe.getId());
        recipeDTO.setRating(averageRating != null ? averageRating : 0);

//        recipeDTO.setIngredients(
//                recipe.getIngredients().stream()
//                        .map(ingredient -> ingredient.getName())
////                        .map(ingredient -> ingredient.getQuantity()+ ": " + ingredient.getName())
//                        .collect(Collectors.toList())
//        );

        recipeDTO.setIngredients(
                recipe.getIngredients().stream()
                        .map(ingredient -> ingredient.getName())
//                        .map(ingredient -> ingredient.getQuantity()+ ": " + ingredient.getName())
                        .collect(Collectors.toList())
        );

        return recipeDTO;
    }

    public Recipe mapToEntity(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setCategory(recipeDTO.getCategory());
//        recipe.setIngredients(recipeDTO.getIngredients());
        recipe.setDirections(recipeDTO.getDirections());
        recipe.setTime(recipeDTO.getTime());
        recipe.setFavorite(recipeDTO.isFavorite());
        recipe.setPicture(recipeDTO.getPicture());
        recipe.setAllergens(recipeDTO.getAllergens());
        recipe.setRating(recipeDTO.getRating());

        List<Ingredient> ingredients = ingredientService.saveAll(
                recipeDTO.getIngredients().stream().map(name -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(name);
                    return ingredient;
                }).collect(Collectors.toList())
        );

//        recipe.setIngredients(
//                recipeDTO.getIngredients().stream()
//                        .map(this::mapDTOToIngredient)
//                        .collect(Collectors.toList())
//        );
        recipe.setIngredients(recipeDTO.getIngredients().stream()
                .map(this::mapDTOToIngredient)
                .collect(Collectors.toList()));

        return recipe;
    }

    public IngredientDTO mapToIngredientDTO(Ingredient ingredient) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setName(ingredient.getName());
        return ingredientDTO;
    }

    private Ingredient mapDTOToIngredient(String ingredientName) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientName);
        return ingredient;
//
//        return ingredientRepository.findByName(ingredientName)
//                .orElseGet(() -> {
//                    Ingredient ingredient = new Ingredient();
//                    ingredient.setName(ingredientName);
//                    return ingredientRepository.save(ingredient);
//                });
    }

    public RecipeDTO createRecipe(Integer userId, RecipeDTO recipeDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Recipe recipe = mapToEntity(recipeDTO);
//        List<Ingredient> savedIngredients = new ArrayList<>();
//        for (String ingredientName : recipeDTO.getIngredients()) {
//            Ingredient ingredient = new Ingredient();
//            ingredient.setName(ingredientName);
//            savedIngredients.add(ingredientRepository.save(ingredient));
//        }
        List<Ingredient> savedIngredients = new ArrayList<>();
        for (String ingredientName : recipeDTO.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseGet(() -> {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(ingredientName);
                        return ingredientRepository.save(newIngredient);
                    });
            savedIngredients.add(ingredient);
        }


        recipe.setUser(user);
        recipe.setIngredients(savedIngredients);
        Recipe savedRecipe = recipeRepository.save(recipe);

        return mapToDTO(savedRecipe);
    }

    public Optional<RecipeDTO> findById(Integer id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipe.map(this::mapToDTO);
    }

    public Page<RecipeDTO> findAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public Page<RecipeDTO> findAll(Specification<Recipe> spec, Pageable pageable) {
        return recipeRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    public RecipeDTO updateRecipe(Integer id, RecipeDTO recipeDTO, Integer userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not allowed to update this recipe");
        }

        Recipe updatedRecipe = mapToEntity(recipeDTO);
        recipe.setName(updatedRecipe.getName());
        recipe.setDescription(updatedRecipe.getDescription());
        recipe.setCategory(updatedRecipe.getCategory());

        List<Ingredient> updatedIngredients = new ArrayList<>();
        for (String ingredientName : recipeDTO.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseGet(() -> {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(ingredientName);
                        return ingredientRepository.save(newIngredient);
                    });
            updatedIngredients.add(ingredient);
        }
        recipe.setIngredients(updatedRecipe.getIngredients());
        recipe.setDirections(updatedRecipe.getDirections());
        recipe.setTime(updatedRecipe.getTime());
        recipe.setFavorite(updatedRecipe.getFavorite());
        recipe.setPicture(updatedRecipe.getPicture());
        recipe.setAllergens(updatedRecipe.getAllergens());
        recipe.setRating(updatedRecipe.getRating());

        return mapToDTO(recipeRepository.save(recipe));
    }

    public void deleteRecipe(Integer id, Integer userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not allowed to delete this recipe");
        }

        recipeRepository.deleteById(id);
    }

    public Page<RecipeDTO> searchRecipes(Specification<Recipe> spec, Pageable pageable) {
        return recipeRepository.findAll(spec, pageable).map(this::mapToDTO);
    }

    public Page<RecipeDTO> getRecipesByName(String name, Pageable pageable) {
        return recipeRepository.findByNameContaining(name, pageable).map(this::mapToDTO);
    }

    public Page<RecipeDTO> getRecipesByIngredient(String ingredientName, Pageable pageable) {
        Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
        return recipeRepository.findByIngredientsNameContaining(ingredient, pageable).map(this::mapToDTO);
    }

    public Page<RecipeDTO> getRecipesByUser(Integer userId, Pageable pageable) {
        return recipeRepository.findByUserId(userId, pageable).map(this::mapToDTO);
    }

    public Double findAverageRatingByRecipeId(Integer recipeId) {
        return reviewRepository.findAverageRatingByRecipeId(recipeId);
    }

    public List<RecipeDTO> getRandomRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        Collections.shuffle(recipes);

        Random rand = new Random();
        int limit = rand.nextInt(recipes.size()) + 1;

        return recipes.stream()
                .map(this::mapToDTO)
                .limit(limit)
                .collect(Collectors.toList());
    }
}