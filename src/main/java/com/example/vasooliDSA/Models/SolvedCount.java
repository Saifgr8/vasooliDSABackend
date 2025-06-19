package com.example.vasooliDSA.Models;

import org.springframework.stereotype.Component;

@Component
public class SolvedCount {
    private int easy;
    private int medium;
    private int hard;

    public SolvedCount(){

    }
    public SolvedCount(int easy, int medium, int hard){
        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
    }

    public int getEasy() {
        return easy;
    }

    public void setEasy(int easy) {
        this.easy = easy;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    @Override
    public String toString() {
        return "SolvedCount{" +
                "easy=" + easy +
                ", medium=" + medium +
                ", hard=" + hard +
                '}';
    }
}
