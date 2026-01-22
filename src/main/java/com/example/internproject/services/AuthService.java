package com.example.internproject.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.security.UserPrincipal;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public String login(String email, String password) {
        Authentication auth;
    	  try {
    		   auth = authManager.authenticate(
    	                new UsernamePasswordAuthenticationToken(email, password)
    	        );
          } catch (BadCredentialsException e) {
              throw new RuntimeException("Invalid email or password");
          }
       
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        return jwtService.generateToken(
            principal.getUsername(),  
            principal.getRole()       
        );
    }
}
