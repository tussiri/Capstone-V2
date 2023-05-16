package org.launchcode.LiftoffRecipeProject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

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
    private LocalDate dateOfBirth;

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

}
