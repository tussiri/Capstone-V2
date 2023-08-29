package org.launchcode.LiftoffRecipeProject.DTO;

public class RecipeIngredientDTO {

    private IngredientDTO ingredient;

    private String quantity;

    public IngredientDTO getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientDTO ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
