package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer>, CrudRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {
    List<Recipe> findAll();
}
