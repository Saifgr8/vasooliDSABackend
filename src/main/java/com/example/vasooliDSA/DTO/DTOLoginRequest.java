package com.example.vasooliDSA.DTO;

import jakarta.validation.constraints.NotBlank;

public class DTOLoginRequest {

    @NotBlank(message = "Email cannot be empty.")
    private String Email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;

    public DTOLoginRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "DTOLoginRequest{" +
                "Email='" + Email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
