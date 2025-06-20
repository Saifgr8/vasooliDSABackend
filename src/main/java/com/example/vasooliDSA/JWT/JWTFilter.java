package com.example.vasooliDSA.JWT;


import com.example.vasooliDSA.SpringSecurity.MyUserDetailServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private final JWTUtils jwtUtils;
    private final MyUserDetailServices myUserDetailServices;

    public JWTFilter(JWTUtils jwtUtils, MyUserDetailServices myUserDetailServices){
        this.jwtUtils = jwtUtils;
        this.myUserDetailServices = myUserDetailServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //extract jwt
            String jwt = jwtUtils.extractJWTFromHeader(request);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                String email = jwtUtils.getEmailFromJWT(jwt);
                UserDetails userDetails = myUserDetailServices.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot authenticate user: {}", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
