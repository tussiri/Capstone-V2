package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.SearchCriteria;
import org.launchcode.LiftoffRecipeProject.services.IngredientService;
import org.launchcode.LiftoffRecipeProject.services.RecipeService;
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

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService recipeService;

    private IngredientService ingredientService;

    @Autowired
    public RecipeController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
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
    @GetMapping("/ingredient/{ingredientName}")
    public ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> getRecipesByIngredient(@PathVariable String ingredientName, Pageable pageable) {
        Page<RecipeDTO> recipeDTOs = recipeService.getRecipesByIngredient(ingredientName, pageable);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipes returned successfully", recipeDTOs), HttpStatus.OK);
    }

    //GET /recipes/{id} return specific recipes by their {id}
    @GetMapping("/{recipeId}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> getRecipe(@PathVariable Integer recipeId) {
        RecipeDTO recipeDTO = recipeService.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe retrieved successfully", recipeDTO), HttpStatus.OK);
    }

    // POST /recipes/userID  creates a new recipes and associates it with a specific user
    @Transactional
    @PostMapping("/{userId}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> createRecipe(@PathVariable Integer userId, @Valid @RequestBody RecipeDTO recipeDTO) {
        RecipeDTO savedRecipeDTO = recipeService.createRecipe(userId, recipeDTO);
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "Recipe created successfully", savedRecipeDTO), HttpStatus.CREATED);
    }

    //PUT /recipes/{id} updates the recipe with the matching {id}
    @Transactional
    @PutMapping("/update/{recipeId}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> updateRecipe(@Valid @PathVariable Integer recipeId, @RequestBody RecipeDTO recipeDTO) {
        RecipeDTO updatedRecipeDTO = recipeService.updateRecipe(recipeId, recipeDTO);

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe updated successfully", updatedRecipeDTO), HttpStatus.OK);
    }

    //DELETE /recipes/{id}  deletes an existing recipe.
    @DeleteMapping("/delete/{recipeId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteRecipe(@PathVariable Integer recipeId) {
        recipeService.deleteRecipe(recipeId);

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Recipe deleted successfully", null), HttpStatus.NO_CONTENT);
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
    public ResponseEntity<List<Recipe>> getRandomRecipes(@RequestParam(defaultValue = "5") int numRecipes) {
        List<Recipe> recipes = recipeService.getRandomRecipes(numRecipes);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }


}