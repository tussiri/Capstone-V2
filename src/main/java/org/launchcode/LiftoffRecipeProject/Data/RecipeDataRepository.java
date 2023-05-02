package org.launchcode.LiftoffRecipeProject.Data;

import org.launchcode.LiftoffRecipeProject.models.RecipeData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeDataRepository extends CrudRepository<RecipeData,Integer> {
}
