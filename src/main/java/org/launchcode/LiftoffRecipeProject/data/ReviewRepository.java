package org.launchcode.LiftoffRecipeProject.data;

import jakarta.transaction.Transactional;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer>, JpaRepository<Review, Integer> {
    List<Review> findByRecipeId(Integer recipeId);
    List<Review> findByUserId(Integer userId);


    @Query("SELECT AVG(r.rating)FROM Review r WHERE r.recipe.id = :recipeId")
    Double findAverageRatingByRecipeId(@Param("recipeId") Integer recipeId);
//
//    void deleteByRecipeIdInAndUserId(List<Integer> recipeIds, Integer userId);
//
//    void deleteByUserId(Integer userId);
//
//
//    @Query("SELECT AVG(r.rating)FROM Review r WHERE r.recipe.id = :recipeId")
//    Double findAverageRatingByRecipeId(@Param("recipeId") Integer recipeId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.user.id = :userId AND r.recipe.id IN :recipeIds")
    void deleteByRecipeIdInAndUserId(@Param("recipeIds") List<Integer> recipeIds, @Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.user.id = :userId")
    void deleteByUserId(@Param("userId") Integer userId);
}