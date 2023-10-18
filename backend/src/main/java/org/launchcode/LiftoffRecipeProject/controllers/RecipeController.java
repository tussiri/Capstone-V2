package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.RecipeNotFoundException;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.SearchCriteria;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.launchcode.LiftoffRecipeProject.services.IngredientService;
import org.launchcode.LiftoffRecipeProject.services.RecipeService;
import org.launchcode.LiftoffRecipeProject.services.UserService;
import org.launchcode.LiftoffRecipeProject.specification.RecipeSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    public static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService recipeService;
    private final UserService userService;
    private final UserRepository userRepository;
    private IngredientService ingredientService;
    private final ReviewRepository reviewRepository;

    @Autowired
    public RecipeController(RecipeService recipeService, UserRepository userRepository, IngredientService ingredientService, ReviewRepository reviewRepository, UserService userService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.reviewRepository = reviewRepository;
        this.userService=userService;
        this.userRepository=userRepository;
    }

    //GET /recipes-returns all recipes
    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> getAllRecipes(Pageable pageable) {
        Page<RecipeDTO> recipeDTOs = recipeService.findAllRecipes(pageable);
        List<RecipeDTO> recipeList = recipeDTOs.getContent();

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All recipes returned successfully", recipeDTOs), HttpStatus.OK);
    }

    //GET /recipes/user/{userId} returns all of the recipes for the user that has logged in.
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> getRecipesByUser(@PathVariable Integer userId, Pageable pageable) {
        Page<RecipeDTO> recipeDTOs = recipeService.getRecipesByUser(userId, pageable);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipes returned successfully", recipeDTOs), HttpStatus.OK);
    }

    // GET /recipes/ingredient/{ingredientName} -return all recipes with this {ingredientName}
//    @GetMapping("/ingredient/{ingredientName}")
//    public ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> getRecipesByIngredient(@PathVariable String ingredientName, Pageable pageable) {
//        Page<RecipeDTO> recipeDTOs = recipeService.getRecipesByIngredient(ingredientName, pageable);
//        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipes returned successfully", recipeDTOs), HttpStatus.OK);
//    }

    //GET /recipes/{id} return specific recipes by their {id}
    @GetMapping("/{recipeId}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> getRecipe(@PathVariable Integer recipeId) {
        RecipeDTO recipeDTO = recipeService.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe retrieved successfully", recipeDTO), HttpStatus.OK);
    }

    // POST /recipes/userID  creates a new recipes and associates it with a specific user
    @Transactional
    @PostMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> createRecipe(@PathVariable Integer userId, @Valid @RequestBody RecipeDTO recipeDTO) {
        RecipeDTO savedRecipeDTO = recipeService.createRecipe(userId, recipeDTO);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "Recipe created successfully", savedRecipeDTO), HttpStatus.CREATED);
    }

    // For multiple recipes
    @Transactional
    @PostMapping("/{userId}/multiple")
    public ResponseEntity<ResponseWrapper<List<RecipeDTO>>> createMultipleRecipes(@PathVariable Integer userId, @Valid @RequestBody List<RecipeDTO> recipeDTOs) {
        List<RecipeDTO> savedRecipeDTOs = recipeService.createRecipes(userId, recipeDTOs);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "Recipes created successfully", savedRecipeDTOs), HttpStatus.CREATED);
    }

    //PUT /recipes/{id} updates the recipe with the matching {id}
    @Transactional
    @PutMapping("/update/{recipeId}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> updateRecipe(@Valid @PathVariable Integer recipeId, @RequestBody RecipeDTO recipeDTO, @RequestHeader("userId") Integer userId) {
        RecipeDTO updatedRecipeDTO = recipeService.updateRecipe(recipeId, recipeDTO, userId);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe updated successfully", updatedRecipeDTO), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> searchRecipes(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "ingredients", required = false) String ingredients,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "rating", required = false) String rating,
            Pageable pageable) {

        Specification<Recipe> spec = Specification.where(null);

        logger.info("Search parameters: name={}, category={}, ingredients={}, time={}, rating={}",
                name, category, ingredients, time, rating);

        if (name != null) {
            spec = spec.and(new RecipeSpecification(new SearchCriteria("name", ":", name)));
        }
        if (category != null) {
            spec = spec.and(new RecipeSpecification(new SearchCriteria("category", ":", category)));
        }
        if (ingredients != null) {
            String[] ingredientNames = ingredients.split(",");
            for (String ingredientName : ingredientNames) {
                spec = spec.and(new RecipeSpecification(new SearchCriteria("ingredients", ":", ingredientName.trim())));
            }
        }
        if (time != null) {
            if (time.startsWith(">")) {
                spec = spec.and(new RecipeSpecification(new SearchCriteria("time", ">", time.substring(1))));
            } else if (time.startsWith("<")) {
                spec = spec.and(new RecipeSpecification(new SearchCriteria("time", "<", time.substring(1))));
            }
        }
        if (rating != null) {
            if (rating.startsWith(">")) {
                spec = spec.and(new RecipeSpecification(new SearchCriteria("rating", ">", rating.substring(1))));
            } else if (rating.startsWith("<")) {
                spec = spec.and(new RecipeSpecification(new SearchCriteria("rating", "<", rating.substring(1))));
            }
        }

        Page<RecipeDTO> recipeDTOs = recipeService.searchRecipes(spec, pageable);

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipes returned successfully", recipeDTOs), HttpStatus.OK);
    }

    //GET random recipes if no search parameters are provided.
    @GetMapping("/random")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> getRandomRecipe() {
        RecipeDTO randomRecipe = recipeService.getRandomRecipe();
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Random recipe retrieved successfully", randomRecipe), HttpStatus.OK);
    }

    @DeleteMapping("delete/{recipeId}")
    public ResponseEntity<ResponseWrapper<String>> deleteRecipe(@PathVariable Integer recipeId, @RequestHeader("userId") Integer userId) {
        recipeService.deleteRecipe(recipeId, userId, true);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe deleted successfully", "Recipe deleted"), HttpStatus.OK);
    }




}