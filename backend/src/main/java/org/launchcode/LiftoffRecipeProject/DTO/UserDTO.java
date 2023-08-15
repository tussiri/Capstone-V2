package org.launchcode.LiftoffRecipeProject.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class UserDTO {

    private Integer id;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=8, max=50, message="Password must be between 8 and 50 characters")
    private String password;

    @NotBlank(message="First name cannot be blank")
    @Size(min=1, max=30, message="First name must be between 1 and 30 characters")
    private String firstName;

    @NotBlank(message="Last name cannot be blank")
    @Size(min=1, max=30, message="Last name must be between 1 and 30 characters")
    private String lastName;

    @NotNull(message="Birthdate is required")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate dateOfBirth;

//    private List<Recipe> favoriteRecipes;
    @JsonIdentityReference(alwaysAsId = true) // instead of the entire object, just output the ID
    private List<Recipe> favoriteRecipes;


    private String token;
//    private String sessionToken;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Recipe> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<Recipe> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    //    public String getSessionToken() {
//        return sessionToken;
//    }
//
//    public void setSessionToken(String sessionToken) {
//        this.sessionToken = sessionToken;
//    }
}