package org.launchcode.LiftoffRecipeProject.services;


import jakarta.transaction.Transactional;
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

    private ReviewRepository reviewRepository;

    private IngredientService ingredientService;
    private final UserRepository userRepository;
    private RecipeData recipeData;
    private Ingredient ingredient;

    public Page<Recipe> findPaginated(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public Optional<Recipe> findById(int id) {
        return recipeRepository.findById(id);
    }

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
        recipeDTO.setFavorite(recipe.isFavorite());
        recipeDTO.setPicture(recipe.getPicture());
        recipeDTO.setAllergens(recipe.getAllergens());
        recipeDTO.setRating(recipe.getRating());
//
//        Double averageRating = reviewRepository.findAverageRatingByRecipeId(recipe.getId());
//        recipeDTO.setRating(averageRating != null ? averageRating : 0);

//        recipeDTO.setIngredients(
//                recipe.getIngredients().stream()
//                        .map(ingredient -> ingredient.getName())
////                        .map(ingredient -> ingredient.getQuantity()+ ": " + ingredient.getName())
//                        .collect(Collectors.toList())
//        );

        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients != null) {
            List<IngredientDTO> ingredientDTOS = recipe.getIngredients()
                    .stream()
                    .map(this::mapToIngredientDTO)
                    .collect(Collectors.toList());
            recipeDTO.setIngredients(ingredientDTOS);
        }
            return recipeDTO;
        }

        public Recipe mapToEntity (RecipeDTO recipeDTO){
            Recipe recipe = new Recipe();
            recipe.setName(recipeDTO.getName());
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setCategory(recipeDTO.getCategory());
            recipe.setDirections(recipeDTO.getDirections());
            recipe.setTime(recipeDTO.getTime());
            recipe.setFavorite(recipeDTO.isFavorite());
            recipe.setPicture(recipeDTO.getPicture());
            recipe.setAllergens(recipeDTO.getAllergens());
            recipe.setRating(recipeDTO.getRating());


            List<IngredientDTO> ingredientDTOs = recipeDTO.getIngredients().stream().map(ingredientDto -> {
                IngredientDTO newIngredientDTO = new IngredientDTO();
                newIngredientDTO.setName(ingredientDto.getName());
                return newIngredientDTO;
            }).collect(Collectors.toList());

            List<Ingredient> ingredients = recipeDTO.getIngredients().stream()
                    .map(this::mapDTOToIngredient)
                    .collect(Collectors.toList());
            recipe.setIngredients(ingredients);


            return recipe;
        }

        public Ingredient mapNameToIngredient (String ingredientName){
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName);
            return ingredient;
        }

        public IngredientDTO mapToIngredientDTO (Ingredient ingredient){
            IngredientDTO ingredientDTO = new IngredientDTO();
            ingredientDTO.setId(ingredient.getId());
            ingredientDTO.setName(ingredient.getName());
            return ingredientDTO;
        }

        private Ingredient mapDTOToIngredient (IngredientDTO ingredientDTO){
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientDTO.getName());
            ingredient.setQuantity(ingredientDTO.getQuantity());
            return ingredient;
//
//        return ingredientRepository.findByName(ingredientName)
//                .orElseGet(() -> {
//                    Ingredient ingredient = new Ingredient();
//                    ingredient.setName(ingredientName);
//                    return ingredientRepository.save(ingredient);
//                });
        }

        public RecipeDTO createRecipe (Integer userId, RecipeDTO recipeDTO){
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
            for (IngredientDTO ingredientDto : recipeDTO.getIngredients()) {
                Ingredient ingredient = mapDTOToIngredient(ingredientDto);
                savedIngredients.add(ingredientRepository.save(ingredient));
            }
            recipe.setIngredients(savedIngredients);


            recipe.setUser(user);
            recipe.setIngredients(savedIngredients);
            Recipe savedRecipe = recipeRepository.save(recipe);

            return mapToDTO(savedRecipe);
        }

        public Optional<RecipeDTO> findById (Integer id){
            Optional<Recipe> recipe = recipeRepository.findById(id);
            return recipe.map(this::mapToDTO);
        }

        public Page<RecipeDTO> findAllRecipes (Pageable pageable){
            return recipeRepository.findAll(pageable)
                    .map(this::mapToDTO);
        }

        public Page<RecipeDTO> findAll (Specification < Recipe > spec, Pageable pageable){
            return recipeRepository.findAll(spec, pageable)
                    .map(this::mapToDTO);
        }

        public RecipeDTO updateRecipe (Integer id, RecipeDTO recipeDTO, Integer userId){
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
            for (IngredientDTO ingredientDto : recipeDTO.getIngredients()) {
                String ingredientName = ingredientDto.getName();
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
            recipe.setFavorite(updatedRecipe.isFavorite());
            recipe.setPicture(updatedRecipe.getPicture());
            recipe.setAllergens(updatedRecipe.getAllergens());
            recipe.setRating(updatedRecipe.getRating());

            return mapToDTO(recipeRepository.save(recipe));
        }

//    public void deleteRecipe(Integer id, Integer userId) {
//        Recipe recipe = recipeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
//
//        if (!recipe.getUser().getId().equals(userId)) {
//            throw new UnauthorizedException("You are not allowed to delete this recipe");
//        }
//
//        recipeRepository.deleteById(id);
//    }

        public void deleteRecipe (Integer id, Integer userId, Boolean deleteAssociatedRecipes){
            Recipe recipe = recipeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

            if (!recipe.getUser().getId().equals(userId)) {
                throw new UnauthorizedException("You are not allowed to delete this recipe");
            }

            if (deleteAssociatedRecipes) {
                recipeRepository.delete(recipe);
            } else {
                recipe.setUser(null);
                recipeRepository.save(recipe);
            }
        }

        @Transactional
        public void deleteUser (Integer userId, Boolean deleteRecipes){
            if (!userRepository.existsById(userId)) {
                throw new ResourceNotFoundException("User not found");
            }

            if (deleteRecipes) {
                // Delete all recipes associated with the user
                userRepository.deleteById(userId);
            } else {
                // Remove the association between the user and the recipes
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // Clear the user reference from all recipes
                for (Recipe recipe : user.getRecipes()) {
                    recipe.setUser(null);
                }

                // Save the changes
                userRepository.deleteById(userId);
            }
        }


        public Page<RecipeDTO> searchRecipes (Specification < Recipe > spec, Pageable pageable){
            return recipeRepository.findAll(spec, pageable).map(this::mapToDTO);
        }

        public Page<RecipeDTO> getRecipesByName (String name, Pageable pageable){
            return recipeRepository.findByNameContaining(name, pageable).map(this::mapToDTO);
        }

        public Page<RecipeDTO> getRecipesByIngredient (String ingredientName, Pageable pageable){
            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
            return recipeRepository.findByIngredientsNameContaining(ingredient, pageable).map(this::mapToDTO);
        }

        public Page<RecipeDTO> getRecipesByUser (Integer userId, Pageable pageable){
            return recipeRepository.findByUserId(userId, pageable).map(this::mapToDTO);
        }

        public Double findAverageRatingByRecipeId (Integer recipeId){
            return reviewRepository.findAverageRatingByRecipeId(recipeId);
        }

        public RecipeDTO getRandomRecipe () {
            List<Recipe> recipes = recipeRepository.findAll();
            Collections.shuffle(recipes);
            return this.mapToDTO(recipes.get(0));
        }

    }