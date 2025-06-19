package com.example.vasooliDSA.Services;

import com.example.vasooliDSA.DTO.DTOProblemSaveRequest;
import com.example.vasooliDSA.Exceptions.InvalidCredentialException;
import com.example.vasooliDSA.Exceptions.ProblemAlreadyExistsException;
import com.example.vasooliDSA.Exceptions.ProblemMissingException;
import com.example.vasooliDSA.Exceptions.SolutionMissingException;
import com.example.vasooliDSA.Models.SolvedCount;
import com.example.vasooliDSA.Models.User;
import com.example.vasooliDSA.Models.UserProblem;
import com.example.vasooliDSA.Models.Solutions;
import com.example.vasooliDSA.Repos.UserProblemRepo;
import com.example.vasooliDSA.Repos.UserRepo;
import com.example.vasooliDSA.ServiceInterfaces.IProblemServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.SourceDataLine;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemServices implements IProblemServices {
    private final UserProblemRepo userProblemRepo;
    private final UserRepo userRepo;

    public ProblemServices(UserProblemRepo userProblemRepo, UserRepo userRepo){
        this.userProblemRepo = userProblemRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserProblem saveProblem(DTOProblemSaveRequest request, String userId) {
        //find problem is exists
        Optional<UserProblem> foundProblem = userProblemRepo.findByProblemLinkAndUserId(
                request.getProblemLink(), userId
        );
        Optional<User> foundUser = userRepo.findById(userId);
        if(foundUser.isEmpty()){
            throw new RuntimeException("User details could not be fetched.");
        }

        User user = foundUser.get();
        String problemDifficulty = request.getDifficulty();
        SolvedCount solvedCount = user.getSolvedCount();

        Solutions newSolution = new Solutions(request.getLanguageUsed(), request.getCode(), request.getNotes());

        if(foundProblem.isPresent()){
            UserProblem existingUserProblem = foundProblem.get();

            boolean languageSolutionExists = existingUserProblem.getSolutions().stream()
                    .anyMatch(s -> s.getLanguageUsed().equalsIgnoreCase(request.getLanguageUsed()));

            if(languageSolutionExists){
                throw new ProblemAlreadyExistsException("You have already submitted a solution for this problem in " +
                        request.getLanguageUsed() + ". Please update the existing solution instead.");
            }else{
                existingUserProblem.addSolution(newSolution);
                return userProblemRepo.save(existingUserProblem);
            }
        }

        switch (problemDifficulty.toLowerCase()){
            case "easy" :
                solvedCount.setEasy(solvedCount.getEasy() + 1);
                break;
            case "medium" :
                solvedCount.setMedium(solvedCount.getMedium() + 1);
                break;
            case "hard":
                solvedCount.setHard(solvedCount.getHard() + 1);
                break;
            default:
                System.out.println("Invalid difficulty level: " + problemDifficulty);
                break;
        }
        UserProblem newUserProblem = new UserProblem();
        newUserProblem.setProblemName(request.getProblemName());
        newUserProblem.setProblemLink(request.getProblemLink());
        newUserProblem.setDifficulty(request.getDifficulty());
        newUserProblem.setUserId(userId);
        newUserProblem.addSolution(newSolution);
        newUserProblem.setTimeComplexity(request.getTimeComplexity());
        newUserProblem.setSpaceComplexity(request.getSpaceComplexity());
        UserProblem savedUserProblem  = userProblemRepo.save(newUserProblem);
        user.getSolvedProblems().add(savedUserProblem.getId());
        userRepo.save(user);
        return savedUserProblem;
    }

    @Override
    public List<UserProblem> getAllProblemsForUserService(String userId) {
        return userProblemRepo.findByUserId(userId);
    }

    @Override
    public void deleteProblem(String problemId, String userId) {
        Optional<UserProblem> problem = userProblemRepo.findById(problemId);

        if(problem.isEmpty()){
            throw new ProblemMissingException("The problem you want to delete, doesn't exist");
        }
        UserProblem problemToDelete = problem.get();

        if(!problemToDelete.getUserId().equals(userId)){
            throw new InvalidCredentialException("You are not authorized to delete this problem.");
        }
        userProblemRepo.delete(problemToDelete);
    }

    @Override
    public UserProblem editProblemService(String problemId, DTOProblemSaveRequest request, String userId) {
        Optional<UserProblem> findProblem = userProblemRepo.findById(problemId);

        if(findProblem.isEmpty()){
            throw new ProblemMissingException("Problem to be updated is missing");
        }
        UserProblem updatedProblem = findProblem.get();
        String problemOwner = updatedProblem.getUserId();

        if(!problemOwner.equals(userId)){
            throw new RuntimeException("You are not authorized to make this change");
        }
        updatedProblem.setProblemName(request.getProblemName());
        updatedProblem.setProblemLink(request.getProblemLink());
        updatedProblem.setDifficulty(request.getDifficulty());

        Optional<Solutions> solutionToUpdateOptional = updatedProblem.getSolutions().stream()
                .filter(s -> s.getLanguageUsed().equalsIgnoreCase(request.getLanguageUsed())).findFirst();

        if(solutionToUpdateOptional.isEmpty()){
            throw new SolutionMissingException("Solution for language: " + request.getLanguageUsed() + " is not present, please check again");
        }
        Solutions solutionToUpdate = solutionToUpdateOptional.get();
        solutionToUpdate.setCode(request.getCode());
        solutionToUpdate.setNotes(request.getNotes());

        return userProblemRepo.save(updatedProblem);
    }

}
