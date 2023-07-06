package org.launchcode.LiftoffRecipeProject.models;


import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class RecipeData {

    public static ArrayList<Recipe> findByColumnAndValue(String column, String value, Iterable<Recipe> allRecipes) {

        ArrayList<Recipe> results = new ArrayList<>();
        if (value.equalsIgnoreCase("all")){
            return (ArrayList<Recipe>) allRecipes;
        }
        if (column.equalsIgnoreCase("all")) {
            results = findByValue(value, allRecipes);
            return results;
        }
        for (Recipe recipe : allRecipes) {
            String fieldValue = getFieldValue(recipe, column);
            if (fieldValue != null && fieldValue.toLowerCase().contains(value.toLowerCase())){
                results.add(recipe);
            }
        }

        return results;
    }

    public static String getFieldValue(Recipe recipe, String fieldName){
        String fieldValue;
        if (fieldName.equals("category")){
            fieldValue = recipe.getCategory();
        } else if (fieldName.equals("time")){
            fieldValue = String.valueOf(recipe.getTime());
        } else {
            fieldValue = recipe.getName();
        }

        return fieldValue;
    }

    public static ArrayList<Recipe> findByValue(String value, Iterable<Recipe> allRecipes){
        String searchableValue = value.toLowerCase();
        ArrayList<Recipe> results = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getName().toLowerCase().contains(searchableValue)){
                results.add(recipe);
            } else if (recipe.getCategory().toLowerCase().contains(searchableValue)){
                results.add(recipe);
            } else if (String.valueOf(recipe.getTime()).contains(searchableValue)){
                results.add(recipe);
            } else if (recipe.toString().toLowerCase().contains(searchableValue)){
                results.add(recipe);
            }
        }
        return results;

    }
}