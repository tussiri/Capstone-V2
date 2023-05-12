package org.launchcode.LiftoffRecipeProject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.launchcode.LiftoffRecipeProject.models.Recipe;

import java.time.LocalDate;
import java.util.List;

public class UserWithRecipesDTO {

    private Integer id;

    @NotBlank(message="First name cannot be blank")
    @Size(min=1, max=30, message="First name must be between 1 and 30 characters")
    private String firstName;

    @NotBlank(message="Last name cannot be blank")
    @Size(min=1, max=30, message="Last name must be between 1 and 30 characters")
    private String lastName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message="Birthdate is required")
    private LocalDate dateOfBirth;

    private List<Recipe> recipes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
