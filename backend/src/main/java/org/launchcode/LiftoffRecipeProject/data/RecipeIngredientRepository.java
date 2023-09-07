package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {


    // Find all RecipeIngredients for a specific recipe
    List<RecipeIngredient> findByRecipe(Recipe recipe);

    // Delete all RecipeIngredients for a specific recipe
    void deleteByRecipe(Recipe recipe);
}
