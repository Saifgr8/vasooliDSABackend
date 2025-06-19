package com.example.vasooliDSA.Services;

import com.example.vasooliDSA.DTO.DTOLoginRequest;
import com.example.vasooliDSA.DTO.DTOLoginResponse;
import com.example.vasooliDSA.DTO.DTORegisterRequest;

import com.example.vasooliDSA.Exceptions.InvalidCredentialException;
import com.example.vasooliDSA.Exceptions.UserAlreadyExistsException;
import com.example.vasooliDSA.Exceptions.UsernameAlreadyTakenException;
import com.example.vasooliDSA.JWT.JWTUtils;
import com.example.vasooliDSA.Models.User;
import com.example.vasooliDSA.Repos.UserRepo;
import com.example.vasooliDSA.ServiceInterfaces.IAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtils jwtUtils){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User registerUser(DTORegisterRequest request){
        //check existing user
        if(userRepo.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("User with this email already exists, please log in.");
        }
        if(userRepo.existsByUsername(request.getUsername())){
            throw new UsernameAlreadyTakenException("User with this Username already exists, please choose different username.");
        }

        //create user
        User user = new User(request.getUsername(), request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepo.save(user);
    }

    @Override
    public DTOLoginResponse loginUserViaPassword(DTOLoginRequest request){
        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        }catch (AuthenticationException e){
            throw new InvalidCredentialException("Invalid email or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJWTFromEmail(userDetails);

       User authenticatedUser = userRepo.findByEmail(userDetails.getUsername())
               .orElseThrow(() -> new InvalidCredentialException("Authentication successful but user data not found."));
       return new DTOLoginResponse(jwt, authenticatedUser);
    }
}
