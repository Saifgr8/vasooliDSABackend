package com.example.vasooliDSA.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class DTOProblemSaveRequest {
    @NotBlank(message = "Problem name cannot be empty.")
    @Size(min = 3, max = 255, message = "Problem name must be between 3 and 255 characters.")
    private  String problemName;

    @NotBlank(message = "Problem link cannot be empty.")
    @URL(message = "Problem link must be a valid URL.")
    private  String problemLink;

    @NotBlank(message = "Difficulty cannot be empty.")
    @Size(min = 1, max = 20, message = "Difficulty must be between 1 and 20 characters.")
    private  String difficulty;

    // --- Solution details are now part of this DTO ---
    @NotBlank(message = "Programming language cannot be empty.")
    @Size(min = 1, max = 50, message = "Language must be between 1 and 50 characters.")
    private String languageUsed;

    @NotBlank(message = "Code cannot be empty.")
    @Size(min = 10, message = "Code must be at least 10 characters long.")
    private String code;

    @NotBlank(message = "Time complexity must be provided")
    private String timeComplexity;

    @NotBlank(message = "Space complexity must be provided")
    private String spaceComplexity;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters.")
    private String notes;


    public DTOProblemSaveRequest() {
    }

    public DTOProblemSaveRequest(String problemName, String problemLink, String difficulty, String languageUsed, String code, String notes, String timeComplexity, String spaceComplexity) {
        this.problemName = problemName;
        this.problemLink = problemLink;
        this.difficulty = difficulty;
        this.languageUsed = languageUsed;
        this.code = code;
        this.notes = notes;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
    }


    public String getTimeComplexity() {
        return timeComplexity;
    }

    public void setTimeComplexity(String timeComplexity) {
        this.timeComplexity = timeComplexity;
    }

    public String getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getProblemLink() {
        return problemLink;
    }

    public void setProblemLink(String problemLink) {
        this.problemLink = problemLink;
    }

    public String getLanguageUsed() {
        return languageUsed;
    }

    public void setLanguageUsed(String languageUsed) {
        this.languageUsed = languageUsed;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
