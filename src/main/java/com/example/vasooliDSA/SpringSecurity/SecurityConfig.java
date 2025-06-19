package com.example.vasooliDSA.SpringSecurity;


import com.example.vasooliDSA.JWT.JWTErrorHandling;
import com.example.vasooliDSA.JWT.JWTFilter;
import com.example.vasooliDSA.JWT.JWTUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTErrorHandling jwtErrorHandling;
    private final JWTUtils jwtUtils;
    private final MyUserDetailServices myUserDetailServices;

    public SecurityConfig(JWTErrorHandling jwtErrorHandling, JWTUtils jwtUtils, MyUserDetailServices myUserDetailServices) {
        this.jwtErrorHandling = jwtErrorHandling;
        this.jwtUtils = jwtUtils;
        this.myUserDetailServices = myUserDetailServices;
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtils, myUserDetailServices);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("api/auth/**").permitAll()
                        .requestMatchers("api/user/getAll").permitAll()
                        .anyRequest().authenticated());
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exe -> exe.authenticationEntryPoint(jwtErrorHandling));
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // Add your Vercel frontend URL and keep your local development URL
        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://vasooli-dsa.vercel.app" // Add your Vercel frontend domain here
        ));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
