package org.launchcode.LiftoffRecipeProject.services;

import org.launchcode.LiftoffRecipeProject.data.IngredientRepository;
import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.findByName(ingredient.getName())
                .orElseGet(() -> ingredientRepository.save(ingredient));
    }


    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    public List<Ingredient>findByNameIn(List<String>names){
        return ingredientRepository.findByNameIn(names);
    }

}