package org.launchcode.LiftoffRecipeProject.Data;

import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
}
