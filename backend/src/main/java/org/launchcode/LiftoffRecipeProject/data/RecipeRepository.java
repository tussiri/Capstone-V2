package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.DTO.RecipeDTO;
import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer>, CrudRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {
    List<Recipe> findAll();

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE lower(i.name) LIKE (concat('%', :ingredientName, '%'))")
    Page<RecipeDTO> getRecipesByIngredient(@Param("ingredientName") String ingredientName, Pageable pageable);

    Page<Recipe> findByIngredientsNameContaining(Ingredient ingredient, Pageable pageable);

    Page<Recipe> findByNameContaining(String name, Pageable pageable);

    Page<Recipe> findByUserId(Integer userId, Pageable pageable);

    @Query("SELECT r.recipe FROM Review r GROUP BY r.recipe HAVING AVG(r.rating) > :rating")
    List<Recipe> findRecipesWithRatingGreaterThan(@Param("rating") Double rating);


    void flush();

    List<Recipe> findByUserAndFavorite(User user, boolean favorite);

    Optional<Recipe> findById(Integer recipeId);


    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.recipeIngredients ri JOIN ri.ingredient i WHERE i.name IN :ingredientNames")
    List<Recipe> findByIngredientsNames(@Param("ingredientNames") List<String> ingredientNames);

}