package org.launchcode.LiftoffRecipeProject.services;

import org.launchcode.LiftoffRecipeProject.data.FavoriteRepository;
import org.launchcode.LiftoffRecipeProject.data.RecipeRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.exception.ResourceNotFoundException;
import org.launchcode.LiftoffRecipeProject.models.Favorite;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteRecipeService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public FavoriteRecipeService(FavoriteRepository favoriteRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public Favorite addFavorite(User userId, Recipe recipeId) {
        User user = userRepository.findById(userId.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Recipe recipe = (Recipe) recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Integer favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    public void deleteFavoriteByUserAndRecipe(Integer userId, Integer recipeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        favoriteRepository.delete(favorite);
    }

    public Optional<Favorite> getFavoriteByUserAndRecipe(Integer userId, Integer recipeId) {
        return favoriteRepository.findByUserIdAndRecipeId(userId, recipeId);
    }

}
