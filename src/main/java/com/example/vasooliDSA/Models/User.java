package com.example.vasooliDSA.Models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String profilePicUrl;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private SolvedCount solvedCount;

    private int points;

    private List<String> solvedProblems = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdOn;
    @LastModifiedDate
    private LocalDateTime updatedOn;

    public User() {
    }

    public User(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public  User(String username, String email) {
        this.username = username;
        this.email = email;
        this.points = 0;
        this.solvedCount = new SolvedCount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SolvedCount getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(SolvedCount solvedCount) {
        this.solvedCount = solvedCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<String> getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(List<String> solvedProblems) {
        this.solvedProblems = solvedProblems;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return points == user.points && Objects.equals(id, user.id) && Objects.equals(profilePicUrl, user.profilePicUrl) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(solvedCount, user.solvedCount) && Objects.equals(solvedProblems, user.solvedProblems) && Objects.equals(createdOn, user.createdOn) && Objects.equals(updatedOn, user.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, profilePicUrl, username, email, password, solvedCount, points, solvedProblems, createdOn, updatedOn);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", solvedCount=" + solvedCount +
                ", points=" + points +
                ", solvedProblems=" + solvedProblems +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
