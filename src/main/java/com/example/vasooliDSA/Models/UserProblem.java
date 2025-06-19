package com.example.vasooliDSA.Models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "problems")
@CompoundIndex(name = "unique_problem_per_user_lang",
        def = "{'problemLink' : 1, 'languageUsed': 1, 'userId': 1}",
        unique = true)
public class UserProblem {

    @Id
    private String id;

    private String problemName;

    private String problemLink;

    private String difficulty;

    private String userId;

    private String timeComplexity;

    private String spaceComplexity;

    private List<Solutions> solutions = new ArrayList<>();

    public UserProblem() {
    }

    public UserProblem(String problemName, String problemLink, String difficulty, String userId, Solutions initialSolution, String timeComplexity, String spaceComplexity) {
        this.problemName = problemName;
        this.problemLink = problemLink;
        this.difficulty = difficulty;
        this.userId = userId;
        if(initialSolution != null){
            this.solutions.add(initialSolution);
        }
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

    public void addSolution(Solutions newSolution){
        if(this.solutions == null){
            this.solutions = new ArrayList<>();
        }
        this.solutions.add(newSolution);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemLink() {
        return problemLink;
    }

    public void setProblemLink(String problemLink) {
        this.problemLink = problemLink;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Solutions> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solutions> solutions) {
        this.solutions = solutions;
    }
}
