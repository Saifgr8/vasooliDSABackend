package com.example.vasooliDSA.Contollers;

import com.example.vasooliDSA.DTO.DTOEditUserRequest;
import com.example.vasooliDSA.DTO.DTOLoginResponse;
import com.example.vasooliDSA.Models.User;
import com.example.vasooliDSA.Repos.UserRepo;
import com.example.vasooliDSA.Services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserServices userServices;
    private final UserRepo userRepo;

    public UserController(UserServices userServices, UserRepo userRepo){
        this.userServices = userServices;
        this.userRepo = userRepo;
    }

    @GetMapping("/getAll")
    public List<User> getAllUsersController(){
        return userServices.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getOwner(@PathVariable String userId){
        User user = userServices.getOwnerService(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return new ResponseEntity<>("Session time out", HttpStatus.UNAUTHORIZED);
        }
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> tempUser = userRepo.findByEmail(email);
        if(tempUser.isEmpty()){
            throw new RuntimeException("Unable to find user");
        }
        User user = tempUser.get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<User> editUser(@RequestBody DTOEditUserRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<User> tempUser = userRepo.findByEmail(email);
        if(tempUser.isEmpty()){
            throw new RuntimeException("Unable to find user.");
        }
        User user = tempUser.get();
        User updatedUser = userServices.editUserService(user, request);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
