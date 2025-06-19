package com.example.vasooliDSA.Contollers;

import com.example.vasooliDSA.DTO.DTOProblemSaveRequest;
import com.example.vasooliDSA.Exceptions.InvalidCredentialException;
import com.example.vasooliDSA.Exceptions.ProblemMissingException;
import com.example.vasooliDSA.Models.User;
import com.example.vasooliDSA.Models.UserProblem;
import com.example.vasooliDSA.Repos.UserRepo;
import com.example.vasooliDSA.Services.ProblemServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/problem")
public class ProblemsController {

    private final ProblemServices problemServices;
    private final UserRepo userRepo;

    public ProblemsController(ProblemServices problemServices, UserRepo userRepo){
        this.problemServices = problemServices;
        this.userRepo = userRepo;
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<List<UserProblem>> getAllProblemsForUser(@PathVariable String userId){
        List<UserProblem> list = problemServices.getAllProblemsForUserService(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<UserProblem> saveProblem(@RequestBody DTOProblemSaveRequest request){
        System.out.println("Data in backend is: " + request.toString());
        //get userid;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();

        Optional<User> user = userRepo.findByEmail(email);
        if((user.isEmpty())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User foundUser = user.get();
        String userId = foundUser.getId();
        UserProblem problem = problemServices.saveProblem(request, userId);
         return new ResponseEntity<>(problem, HttpStatus.CREATED);
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<Void> deleteProblemController(@PathVariable String problemId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();

        Optional<User> user = userRepo.findByEmail(email);
        if((user.isEmpty())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User foundUser = user.get();
        String userId = foundUser.getId();
        problemServices.deleteProblem(problemId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<UserProblem> editProblemController(
            @PathVariable String problemId,
            @RequestBody DTOProblemSaveRequest request
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();

        Optional<User> user = userRepo.findByEmail(email);
        if((user.isEmpty())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User foundUser = user.get();
        String userId = foundUser.getId();
        UserProblem data = problemServices.editProblemService(problemId, request, userId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
