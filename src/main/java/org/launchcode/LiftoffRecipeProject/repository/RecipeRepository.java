package org.launchcode.LiftoffRecipeProject.repository;

import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
}
