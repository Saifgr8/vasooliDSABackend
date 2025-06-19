package com.example.vasooliDSA.SpringSecurity;

import com.example.vasooliDSA.Models.User;
import com.example.vasooliDSA.Repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailServices implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailServices(UserRepo repo){
        this.userRepo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> temp = userRepo.findByEmail(username);
        if(temp.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        User user = temp.get();
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build();
    }
}
