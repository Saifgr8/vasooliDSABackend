package com.example.vasooliDSA.DTO;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class DTOErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error; // HTTP status reason phrase (e.g., "Bad Request")
    private String message; // Your specific error message

    public DTOErrorResponse(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
