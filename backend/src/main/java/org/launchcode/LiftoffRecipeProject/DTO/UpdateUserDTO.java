package org.launchcode.LiftoffRecipeProject.DTO;

import jakarta.validation.constraints.Size;

public class UpdateUserDTO {

    @Size(min=1, max=30, message="First name must be between 1 and 30 characters")

    private String firstName;

    @Size(min=1, max=30, message="Last name must be between 1 and 30 characters")

    private String lastName;

    @Size(min=8, max=50, message="Password name must be between 8 and 50 characters")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}