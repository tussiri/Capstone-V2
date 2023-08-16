package org.launchcode.LiftoffRecipeProject.DTO;


/**
 * Data Transfer Object representing an ingredient.
 * This DTO is used to transfer ingredient data between the application layers.
 */
public class IngredientDTO {

    private Integer id;
    private String name;
    private String quantity;


    public IngredientDTO(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public IngredientDTO(Integer id, String name) {
        this.name = name;
    }

    public IngredientDTO() {
    }

    public IngredientDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}