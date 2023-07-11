package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.Favorite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {
    List<Favorite> findByUserId(Integer userId);
//    Favorite findByUserIdAndRecipeId(Integer userId, Integer recipeId);

    Optional<Object> findByUserAndRecipe(Integer user, Integer recipe);

    Optional<Favorite> findByUserIdAndRecipeId(Integer userId, Integer recipeId);

}
