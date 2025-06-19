package com.example.vasooliDSA.Models;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public class Solutions {

    private String languageUsed;

    private String code;

    private String notes;

    @CreatedDate
    private LocalDateTime submittedOn;

    public Solutions() {
    }

    public Solutions(String languageUsed, String code, String notes) {
        this.languageUsed = languageUsed;
        this.code = code;
        this.notes = notes;
        this.submittedOn = LocalDateTime.now();
    }

    public String getLanguageUsed() {
        return languageUsed;
    }

    public void setLanguageUsed(String languageUsed) {
        this.languageUsed = languageUsed;
    }

    public LocalDateTime getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(LocalDateTime submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
