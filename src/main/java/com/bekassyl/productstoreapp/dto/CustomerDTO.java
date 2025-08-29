package com.bekassyl.productstoreapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
    @Size(min = 1, max = 50, message = "Username <= 50 chars!")
    @NotBlank(message = "Username is required!")
    private String username;

    @Size(min = 8, message = "Password >= 8 chars!")
    @NotBlank(message = "Password is required!")
    private String password;

    @Size(max = 100, message = "First name <= 100 chars!")
    @NotBlank(message = "First name is required!")
    private String firstName;

    @Size(max = 100, message = "Last name <= 100 chars!")
    @NotBlank(message = "Last name is required!")
    private String lastName;

    @Size(max = 100, message = "Email <= 100 chars!")
    @NotBlank(message = "Email is required!")
    private String email;

    public CustomerDTO() {
    }
}
