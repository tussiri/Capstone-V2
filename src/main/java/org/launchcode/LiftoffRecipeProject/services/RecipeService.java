package org.launchcode.LiftoffRecipeProject.services;


import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.data.IngredientRepository;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
    }

    public RecipeDTO mapToDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setName(recipe.getName());
        recipeDTO.setDescription(recipe.getDescription());
        recipeDTO.setCategory(recipe.getCategory());
//        recipeDTO.setIngredients(recipe.getIngredients());
        recipeDTO.setDirections(recipe.getDirections());
        recipeDTO.setTime(recipe.getTime());
        recipeDTO.setFavorite(recipe.getFavorite());
        recipeDTO.setPicture(recipe.getPicture());
        recipeDTO.setAllergens(recipe.getAllergens());
        recipeDTO.setRating(recipe.getRating());

        recipeDTO.setIngredients(
                recipe.getIngredients().stream()
                        .map(Ingredient::getName)
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

        recipe.setIngredients(
                recipeDTO.getIngredients().stream()
                        .map(this::mapDTOToIngredient)
                        .collect(Collectors.toList())
        );

        return recipe;
    }

    private Ingredient mapDTOToIngredient(String ingredientName) {
        return ingredientRepository.findByName(ingredientName)
                .orElseGet(() -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(ingredientName);
                    return ingredientRepository.save(ingredient);
                });
    }

    public RecipeDTO createRecipe(Integer userId, RecipeDTO recipeDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Recipe recipe = mapToEntity(recipeDTO);
        recipe.setUser(user);
        Recipe savedRecipe = recipeRepository.save(recipe);

        return mapToDTO(savedRecipe);
    }

    public Optional<RecipeDTO> findById(Integer id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            return Optional.of(mapToDTO(recipe.get()));
        } else {
            return Optional.empty();
        }
    }

    public Page<RecipeDTO> findAllRecipes(Pageable pageable){
        return recipeRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public Page<RecipeDTO> findAll(Specification<Recipe> spec, Pageable pageable) {
        return recipeRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    public RecipeDTO updateRecipe(Integer id, RecipeDTO recipeDTO) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        Recipe updatedRecipe = mapToEntity(recipeDTO);
        recipe.setName(updatedRecipe.getName());
        recipe.setDescription(updatedRecipe.getDescription());
        recipe.setCategory(updatedRecipe.getCategory());
        recipe.setIngredients(updatedRecipe.getIngredients());
        recipe.setDirections(updatedRecipe.getDirections());
        recipe.setTime(updatedRecipe.getTime());
        recipe.setFavorite(updatedRecipe.getFavorite());
        recipe.setPicture(updatedRecipe.getPicture());
        recipe.setAllergens(updatedRecipe.getAllergens());
        recipe.setRating(updatedRecipe.getRating());

        return mapToDTO(recipeRepository.save(recipe));
    }

    public void deleteRecipe(Integer id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }
    }
}
