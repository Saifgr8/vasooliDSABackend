package com.example.vasooliDSA.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

public class DTORegisterRequest {
    @NotBlank(message = "Username cant be empty")
    @Size(message = "Username should be min 3 to max 12 characters", min = 3, max = 12)
    public String username;

    @NotBlank(message = "Email cannot be empty")
    @Email
    public String email;

    @NotBlank(message = "Password cant be empty")
    @Size(message = "Password should be min 3 to max 12 characters", min = 3, max = 12)
    public String password;

    public DTORegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DTORegisterRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
