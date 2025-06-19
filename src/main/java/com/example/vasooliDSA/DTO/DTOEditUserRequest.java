package com.example.vasooliDSA.DTO;

public class DTOEditUserRequest {
    public String username;

    public DTOEditUserRequest(String username) {
        this.username = username;
    }

    public DTOEditUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
