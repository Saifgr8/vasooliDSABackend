package com.example.vasooliDSA.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleOAuthService {

    public static class GoogleUserInfo{
        private String googleId;
        private String email;
        private String name;
        private String picture;

        public GoogleUserInfo(String googleId, String email, String name, String picture) {
            this.googleId = googleId;
            this.email = email;
            this.name = name;
            this.picture = picture;
        }

        public String getGoogleId() {
            return googleId;
        }

        public void setGoogleId(String googleId) {
            this.googleId = googleId;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleSecretKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleOAuthService(RestTemplate restTemplate, ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // This method will verify the Google Access Token and fetch user info
    public GoogleUserInfo verifyGoogleAccessTokenAndGetUserInfo(String accessToken){
        //make the endpoint to talk to google
        String googleTokenIngoURL = UriComponentsBuilder.fromUriString("https://www.googleapis.com/oauth2/v3/userinfo")
                .queryParam("access_token", accessToken)
                .toUriString();

        try {
            //make http request to google endpoint
            String response = restTemplate.getForObject(googleTokenIngoURL, String.class);

            // Parse the JSON response
            JsonNode root = objectMapper.readTree(response);

            String googleId = root.has("sub") ? root.get("sub").asText() : null;
            String email = root.has("email") ? root.get("email").asText() : null;
            String name = root.has("name") ? root.get("name").asText() : null;
            String picture = root.has("picture") ? root.get("picture").asText() : null;

            if(email == null || googleId == null){
                throw new RuntimeException("Could not retrieve essential user info (email or Google ID) from Google token.");
            }
            return new GoogleUserInfo(googleId,email,name,picture);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // Log the error and re-throw a more specific exception if needed
            System.err.println("Error verifying Google access token or fetching user info: " + e.getMessage());
            throw new RuntimeException("Failed to verify Google token or fetch user info.", e);
        }
    }

}

