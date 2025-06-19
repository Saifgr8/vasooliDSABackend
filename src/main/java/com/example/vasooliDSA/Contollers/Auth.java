package com.example.vasooliDSA.Contollers;

import com.example.vasooliDSA.DTO.DTOGoogleLoginRequest;
import com.example.vasooliDSA.DTO.DTOLoginRequest;
import com.example.vasooliDSA.DTO.DTOLoginResponse;
import com.example.vasooliDSA.DTO.DTORegisterRequest;
import com.example.vasooliDSA.JWT.JWTUtils;
import com.example.vasooliDSA.Models.User;
import com.example.vasooliDSA.Repos.UserRepo;
import com.example.vasooliDSA.Services.AuthService;
import com.example.vasooliDSA.Services.GoogleOAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Import this
import org.springframework.security.crypto.password.PasswordEncoder; // Import this

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/auth")
public class Auth {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleSecretKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @Autowired // *** NEW: Inject PasswordEncoder ***
    private PasswordEncoder passwordEncoder;


    private final AuthService authService; // This is constructor injected
    public Auth(AuthService authService){
        this.authService = authService;
    }

    // Your existing registerUser method
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody DTORegisterRequest request){
        System.out.println("API CALL WAS MADE");
        User user = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Your existing loginUserViaPassword method
    @PostMapping("/loginP")
    public ResponseEntity<?> loginUserViaPassword(
            @Valid @RequestBody DTOLoginRequest request) {
        DTOLoginResponse data = authService.loginUserViaPassword(request);
        HttpHeaders http = new HttpHeaders();
        http.add("Authorization", "Bearer " + data.getJwt());
        data.setJwt(null);
        return ResponseEntity.status(HttpStatus.OK).headers(http).body(data);
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> loginUserViaGoogle(@RequestBody DTOGoogleLoginRequest request){
        try {
            String googleAccessToken  = request.getGoogleAccessToken();

            // *** FIX 1: Corrected class name ***
            GoogleOAuthService.GoogleUserInfo userInfo = googleOAuthService.verifyGoogleAccessTokenAndGetUserInfo(googleAccessToken);

            String email = userInfo.getEmail();
            String name = userInfo.getName();
            String picUrl = userInfo.getPicture();

            UserDetails userDetails;
            User user;

            try {
                // Try to load user by email
                userDetails = userDetailsService.loadUserByUsername(email);
                // If loaded, get your actual User entity (assuming MyUserDetailServices returns your User entity or has a way to get it)
                // Or, if your UserDetails implementation contains your User entity, cast it.
                // For simplicity, let's assume userRepo.findByEmail is the source of truth for your User entity
                user = userRepo.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found in DB after loading UserDetails."));
            } catch (UsernameNotFoundException e) {
                // *** FIX 2: User not found, create new user ***
                System.out.println("Google user not found in DB, registering new user: " + email);
                user = new User(name, email);
                user.setProfilePicUrl(picUrl);
                // *** FIX 3: Encode the password ***
                user.setPassword(passwordEncoder.encode("GOOGLE_AUTH_DUMMY_PASSWORD")); // Encode dummy password
                user = userRepo.save(user); // Save the new user
                // After saving, reload userDetails to ensure it's fully populated if needed
                userDetails = userDetailsService.loadUserByUsername(email);
            }


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // userDetails = (UserDetails) authenticationToken.getPrincipal(); // This line is redundant if userDetails is already from loadUserByUsername
            String jwt = jwtUtils.generateJWTFromEmail(userDetails); // Use the userDetails from loadUserByUsername

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer "+ jwt);

            // You might want to return DTOLoginResponse here, not just the JWT string
            // For consistency, let's return a DTOLoginResponse
            DTOLoginResponse loginResponse = new DTOLoginResponse(jwt, user); // Assuming DTOLoginResponse takes JWT and User
            loginResponse.setJwt(null); // Nullify JWT in body as it's in header

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(loginResponse);

        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace to see what's actually failing
            System.err.println("Google Auth Callback Error: " + e.getMessage());
            // Consider using your DTOErrorResponse from GlobalExceptionHandler here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Internal server error during Google login: " + e.getMessage() + "\"}");
        }
    }
}