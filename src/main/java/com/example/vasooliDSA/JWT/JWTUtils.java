package com.example.vasooliDSA.JWT;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;


@Component
public class JWTUtils {

    public static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    @Value("${spring.app.secretKey}")
    private String secretKey;
    @Value("${spring.app.expiration}")
    private int jwtExpiration;

    //extract header
    public String extractJWTFromHeader(HttpServletRequest request){
        System.out.println("TOKEN IN HEADER IS: " + request.getHeader("Authorization"));
        String bearer = request.getHeader("Authorization");
        if(bearer != null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }

    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }
    //generate jwt from email;
    public String generateJWTFromEmail(UserDetails userDetails){
        String email = userDetails.getUsername();
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(key())
                .compact();
    }

    //extract email
    public String getEmailFromJWT(String jwt){
        return Jwts.parser()
                .verifyWith((SecretKey)key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    //validate jwt
    public boolean validateJwtToken(String token) { // Renamed method for clarity
        try {
            Jwts.parser().verifyWith((SecretKey)key()).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) { // Catches invalid signature specifically
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
