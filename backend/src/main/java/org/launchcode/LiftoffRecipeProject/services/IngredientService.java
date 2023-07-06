package org.launchcode.LiftoffRecipeProject.services;

import org.launchcode.LiftoffRecipeProject.DTO.IngredientDTO;
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


    //    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
//        return ingredients.stream()
//                .map(this::save)
//                .collect(Collectors.toList());
//    }
    public List<Ingredient> saveAll(List<IngredientDTO> ingredientDTOs) {
        List<Ingredient> ingredients = ingredientDTOs.stream()
                .map(dto -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(dto.getName());
                    // Do the same for quantity and other fields if needed
                    return save(ingredient);
                })
                .collect(Collectors.toList());
        return ingredients;
    }

    public List<Ingredient> findByNameIn(List<String> names) {
        return ingredientRepository.findByNameIn(names);
    }

    public Ingredient splitIngredientLine(String ingredientLine) {
        String[] parts = ingredientLine.split(",", 2);
        Ingredient ingredient = new Ingredient();

        if (parts.length >= 2) {
            ingredient.setName(parts[1].trim());
        } else {
            ingredient.setName(parts[0].trim());
        }

        return ingredient;
    }

}