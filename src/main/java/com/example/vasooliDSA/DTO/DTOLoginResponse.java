package com.example.vasooliDSA.DTO;

import com.example.vasooliDSA.Models.SolvedCount;
import com.example.vasooliDSA.Models.User;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

public class DTOLoginResponse {
    private String jwt;
    private String id;
    private String username;
    private String email;
    private SolvedCount solvedCount;
    private List<String> problemSolved;
    private String profileUrl;
    private int points;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public DTOLoginResponse() {
    }

    public DTOLoginResponse(String jwt, User user) {
        this.jwt = jwt;
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.solvedCount = user.getSolvedCount();
        this.problemSolved = user.getSolvedProblems();
        this.profileUrl = user.getProfilePicUrl();
        this.points = user.getPoints();
        this.createdOn = user.getCreatedOn();
        this.updatedOn = user.getUpdatedOn();
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public List<String> getProblemSolved() {
        return problemSolved;
    }

    public void setProblemSolved(List<String> problemSolved) {
        this.problemSolved = problemSolved;
    }

    public SolvedCount getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(SolvedCount solvedCount) {
        this.solvedCount = solvedCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
