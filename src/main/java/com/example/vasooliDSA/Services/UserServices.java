package com.example.vasooliDSA.Services;

import com.example.vasooliDSA.DTO.DTOEditUserRequest;
import com.example.vasooliDSA.DTO.DTOLoginResponse;
import com.example.vasooliDSA.Models.User;
import com.example.vasooliDSA.Repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

    private final UserRepo userRepo;

    public UserServices(UserRepo userRepo){
        this.userRepo = userRepo;
    }
    public List<User> getAllUsers(){
        try {
            return userRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to retrieve list of all users.");
        }
    }

    public User getOwnerService(String id){
        try {
            Optional<User> tempUser = userRepo.findById(id);
            if(tempUser.isEmpty()){
                throw new RuntimeException("No user found");
            }
            return tempUser.get();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public User editUserService(User tempUser, DTOEditUserRequest request){
        try {
            tempUser.setUsername(request.getUsername());
            userRepo.save(tempUser);
            return tempUser;
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
