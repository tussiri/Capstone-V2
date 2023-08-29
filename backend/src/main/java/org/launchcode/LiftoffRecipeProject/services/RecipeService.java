package org.launchcode.LiftoffRecipeProject.services;


import jakarta.transaction.Transactional;
import org.launchcode.LiftoffRecipeProject.DTO.IngredientDTO;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.data.*;
import org.launchcode.LiftoffRecipeProject.exception.RecipeNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.UnauthorizedException;
import org.launchcode.LiftoffRecipeProject.models.*;
import org.launchcode.LiftoffRecipeProject.specification.RecipeSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private RecipeIngredientRepository recipeIngredientRepository;

    private ReviewRepository reviewRepository;

    private IngredientService ingredientService;
    private final UserRepository userRepository;
    private RecipeData recipeData;
    private Ingredient ingredient;

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);


    public Page<Recipe> findPaginated(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public Optional<Recipe> findById(int id) {
        return recipeRepository.findById(id);
    }

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, UserRepository userRepository, RecipeData recipeData, IngredientService ingredientService, ReviewRepository reviewRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.recipeData = recipeData;
        this.ingredientService = ingredientService;
        this.reviewRepository = reviewRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
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

        List<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();
        if (recipeIngredients != null) {
            List<IngredientDTO> ingredientDTOS = recipeIngredients
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

//
//            List<IngredientDTO> ingredientDTOs = recipeDTO.getIngredients().stream().map(ingredientDto -> {
//                IngredientDTO newIngredientDTO = new IngredientDTO();
//                newIngredientDTO.setName(ingredientDto.getName());
//                return newIngredientDTO;
//            }).collect(Collectors.toList());
            List<IngredientDTO> ingredientDTOs = recipeDTO.getIngredients();
            if (ingredientDTOs != null) {
                List<RecipeIngredient> recipeIngredients = ingredientDTOs.stream()
                        .map(this::mapDTOToIngredient)
                        .collect(Collectors.toList());
                recipe.setRecipeIngredients(recipeIngredients);
            }

            return recipe;
        }

        public Ingredient mapNameToIngredient (String ingredientName){
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName);
            return ingredient;
        }

        public IngredientDTO mapToIngredientDTO (RecipeIngredient recipeIngredient){
            IngredientDTO ingredientDTO = new IngredientDTO();
            ingredientDTO.setId(recipeIngredient.getIngredient().getId());
            ingredientDTO.setName(recipeIngredient.getIngredient().getName());
            ingredientDTO.setQuantity(recipeIngredient.getQuantity());
            return ingredientDTO;
        }

        private RecipeIngredient mapDTOToIngredient (IngredientDTO ingredientDTO){
            RecipeIngredient recipeIngredient = new RecipeIngredient();
//            ingredient.setName(ingredientDTO.getName());
//            ingredient.setQuantity(ingredientDTO.getQuantity());
//            return ingredient;
//
//        return ingredientRepository.findByName(ingredientName)
//                .orElseGet(() -> {
//                    Ingredient ingredient = new Ingredient();
//                    ingredient.setName(ingredientName);
//                    return ingredientRepository.save(ingredient);
//                });
            Ingredient ingredient = ingredientRepository.findByName(ingredientDTO.getName())
                    .orElseGet(() -> {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(ingredientDTO.getName());
                        return ingredientRepository.save(newIngredient);
                    });
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(ingredientDTO.getQuantity());
            return recipeIngredient;
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

            recipe.setUser(user);
            recipe = recipeRepository.save(recipe);

            for (IngredientDTO ingredientDto : recipeDTO.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findByName(ingredientDto.getName())
                        .orElseGet(() -> {
                            Ingredient newIngredient = new Ingredient();
                            newIngredient.setName(ingredientDto.getName());
                            return ingredientRepository.save(newIngredient);
                        });

                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipe(recipe);  // Reference to the saved recipe
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setQuantity(ingredientDto.getQuantity());
                recipeIngredientRepository.save(recipeIngredient);  // Save RecipeIngredient
            }

            return mapToDTO(recipe);  // Return the saved recipe as DTO
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

        public RecipeDTO updateRecipe (Integer recipeId, RecipeDTO recipeDTO, Integer userId){
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

            System.out.println("In updateRecipe method, Recipe object: " + recipe);


            if (!recipe.getUser().getId().equals(userId)) {
                throw new UnauthorizedException("You are not allowed to update this recipe");
            }

            recipeIngredientRepository.deleteByRecipe(recipe);


            Recipe updatedRecipe = mapToEntity(recipeDTO);
            recipe.setName(updatedRecipe.getName());
            recipe.setDescription(updatedRecipe.getDescription());
            recipe.setCategory(updatedRecipe.getCategory());

            for (IngredientDTO ingredientDto : recipeDTO.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findByName(ingredientDto.getName())
                        .orElseGet(() -> {
                            Ingredient newIngredient = new Ingredient();
                            newIngredient.setName(ingredientDto.getName());
                            return ingredientRepository.save(newIngredient);
                        });

                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipe(recipe);
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setQuantity(ingredientDto.getQuantity());
                recipeIngredientRepository.save(recipeIngredient);
            }
            recipe.setDirections(updatedRecipe.getDirections());
            recipe.setTime(updatedRecipe.getTime());
            recipe.setFavorite(updatedRecipe.isFavorite());
            recipe.setPicture(updatedRecipe.getPicture());
            recipe.setAllergens(updatedRecipe.getAllergens());
            recipe.setRating(updatedRecipe.getRating());

            return mapToDTO(recipeRepository.save(recipe));
        }

    @Transactional
    public void deleteRecipe(Integer id, Integer userId, Boolean deleteAssociatedRecipes) {

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Recipe not found");
                });

        if (!recipe.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not allowed to delete this recipe");
        }

        if (deleteAssociatedRecipes) {
            recipeIngredientRepository.deleteByRecipe(recipe);  // Delete associated ingredients
            recipeRepository.delete(recipe);  // Delete the recipe
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




        public Page<RecipeDTO> searchRecipes (Specification <Recipe> spec, Pageable pageable){
            return recipeRepository.findAll(spec, pageable).map(this::mapToDTO);

        }

    public List<Recipe> searchRecipes() {
        Specification<Recipe> spec = new RecipeSpecification(new SearchCriteria("ingredients", ":", "sugar"));
        return recipeRepository.findAll(spec);
    }

        public Page<RecipeDTO> getRecipesByName (String name, Pageable pageable){
            return recipeRepository.findByNameContaining(name, pageable).map(this::mapToDTO);
        }

    public Page<RecipeDTO> getRecipesByIngredient(String ingredientName, Pageable pageable) {
        Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
        Page<RecipeDTO> recipeDTOs = recipeRepository.findByIngredientsNameContaining(ingredient, pageable).map(this::mapToDTO);
        logger.info("Recipes found by ingredient {}: {}", ingredientName, recipeDTOs.getContent());
        return recipeDTOs;
    }

    public List<Recipe> searchByCriteria(SearchCriteria criteria) {
        Specification<Recipe> specification = new RecipeSpecification(criteria);
        return recipeRepository.findAll(specification);
    }

    public List<Recipe> searchByIngredient(String ingredients) {
        List<String> ingredientNames = Arrays.asList(ingredients.split(","));
        return recipeRepository.findByIngredientsNames(ingredientNames);
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