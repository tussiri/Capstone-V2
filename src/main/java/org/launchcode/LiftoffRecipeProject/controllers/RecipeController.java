package org.launchcode.LiftoffRecipeProject.controllers;


import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.DTO.ResponseWrapper;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.SearchCriteria;
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

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private static final Logger logger= LoggerFactory.getLogger(RecipeController.class);

    private RecipeService recipeService;
    private RecipeService userRepository;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<RecipeDTO>>> getAllRecipes(Pageable pageable) {
        Page<RecipeDTO> recipeDTOs = recipeService.findAllRecipes(pageable);
        List<RecipeDTO> recipeList=recipeDTOs.getContent();

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "All recipes returned successfully", recipeDTOs), HttpStatus.OK);
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
            spec = spec.and(new RecipeSpecification(new SearchCriteria("ingredients", ":", ingredients)));
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
        System.out.println("Executing search with spec: " + spec.toString());

        Page<RecipeDTO> recipeDTOs = recipeService.findAll(spec, pageable);
        

        System.out.println("Found " +recipeDTOs.getTotalElements() + " recipes");

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipes returned successfully", recipeDTOs), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> getRecipe(@PathVariable Integer id) {
        RecipeDTO recipeDTO = recipeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe retrieved successfully", recipeDTO), HttpStatus.OK);
    }


    @PostMapping("/{userId}")
public ResponseEntity<ResponseWrapper<RecipeDTO>> createRecipe(@PathVariable Integer userId, @Valid @RequestBody RecipeDTO recipeDTO) {
    RecipeDTO savedRecipeDTO = recipeService.createRecipe(userId, recipeDTO);
    return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.CREATED.value(), "Recipe created successfully", savedRecipeDTO), HttpStatus.CREATED);
}

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<RecipeDTO>> updateRecipe(@Valid @PathVariable Integer id, @RequestBody RecipeDTO recipeDTO) {
        RecipeDTO updatedRecipeDTO = recipeService.updateRecipe(id, recipeDTO);

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK.value(), "Recipe updated successfully", updatedRecipeDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteRecipe(@PathVariable Integer id) {
        recipeService.deleteRecipe(id);

        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Recipe deleted successfully", null), HttpStatus.NO_CONTENT);
    }
}
