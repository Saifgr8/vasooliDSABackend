package com.example.vasooliDSA.ServiceInterfaces;

import com.example.vasooliDSA.DTO.DTOProblemSaveRequest;
import com.example.vasooliDSA.Models.UserProblem;

import java.util.List;

public interface IProblemServices {

    UserProblem saveProblem(DTOProblemSaveRequest request, String userId);

    List<UserProblem> getAllProblemsForUserService(String userId);

    void deleteProblem(String problemId, String userId);

    UserProblem editProblemService(String problemId, DTOProblemSaveRequest request, String userId);
}
