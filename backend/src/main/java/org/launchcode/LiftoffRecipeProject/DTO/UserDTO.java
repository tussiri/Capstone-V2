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
import java.util.Objects;

/**
 * UserDTO
 * Purpose: Represents a user with all their associated details.
 *
 * Fields:
 *
 * id: Unique identifier for the user.
 * email: The email address of the user.
 * password: The password for the user.
 * firstName: The first name of the user.
 * lastName: The last name of the user.
 * dateOfBirth: The date of birth of the user.
 * token: A token that might be used for session management or authentication purposes.
 */
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
    private List<RecipeDTO> favoriteRecipes;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(email, userDTO.email) &&
                Objects.equals(password, userDTO.password) &&
                Objects.equals(firstName, userDTO.firstName) &&
                Objects.equals(lastName, userDTO.lastName) &&
                Objects.equals(dateOfBirth, userDTO.dateOfBirth) &&
                Objects.equals(token, userDTO.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, firstName, lastName, dateOfBirth, token);
    }

    //    public String getSessionToken() {
//        return sessionToken;
//    }
//
//    public void setSessionToken(String sessionToken) {
//        this.sessionToken = sessionToken;
//    }
}