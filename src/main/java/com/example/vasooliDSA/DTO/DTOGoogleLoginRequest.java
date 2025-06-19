package com.example.vasooliDSA.DTO;

import jakarta.validation.constraints.NotBlank;

public class DTOGoogleLoginRequest {
    @NotBlank(message = "Google Access token cannot be empty")
    private String googleAccessToken; // Field name

    public DTOGoogleLoginRequest() {}
    public DTOGoogleLoginRequest(String googleAccessToken) { this.googleAccessToken = googleAccessToken; }

    public String getGoogleAccessToken() { return googleAccessToken; } // Getter
    public void setGoogleAccessToken(String googleAccessToken) { this.googleAccessToken = googleAccessToken; }
}