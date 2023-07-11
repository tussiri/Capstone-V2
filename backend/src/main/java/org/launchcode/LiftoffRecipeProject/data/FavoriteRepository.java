package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.Favorite;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {
    List<Favorite> findByUserId(Integer userId);

    Optional<Favorite> findByUserIdAndRecipeId(Integer userId, Integer recipeId);

    List<Favorite> findByUser(User user);

//    @Query("SELECT f FROM Favorite f JOIN FETCH f.recipe WHERE f.user.id = :userId")
//    List<Favorite> findByUserIdWithRecipes(@Param("userId") Integer userId);

    @Query("SELECT f FROM Favorite f LEFT JOIN FETCH f.recipe WHERE f.user.id = :userId")
    List<Favorite> findByUserIdWithRecipes(@Param("userId") Integer userId);


}