package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findByRecipeId(Integer recipeId);
}