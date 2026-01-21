package com.example.internproject.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional 
class UserServiceTest {

	@Autowired
    private UserService userService;
	@Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    @DisplayName("User registered successfully")
    void shouldRegisterUserSuccessfullyWithPassword() {
    	
    	String name = "John Doe";
        String email = "john@test.com";
        String password = "secret123";
        String phone = "123456789";
        Role role = Role.CUSTOMER;
        
        User user = userService.register(name, email, password, phone, role);
        
        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());

        // Password should NOT be stored as plain text
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }
    
    @Test
    void shouldFailIfEmailAlreadyExists() {
    	userService.register(
    	        "John",
    	        "duplicate@test.com",
    	        "password",
    	        "123",
    	        Role.CUSTOMER
    	    );

    	assertThrows(RuntimeException.class, () -> {
            userService.register(
                "Another John",
                "duplicate@test.com",
                "password2",
                "456",
                Role.CUSTOMER
            );
        });
    }
    
    @Test
    void shouldReturnTokenIfSuccesfullLogin() {
        String email = "123@test.com";
        String password = "1234";

        userService.register(
            "Login User",
            email,
            password,
            "999",
            Role.CUSTOMER
        );

        String token = authService.login(email, password);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }
    
    @Test
    void shouldFailIfWrongPassword() {
        String email = "123@test.com";

        userService.register(
            "User",
            email,
            "1234",
            "000",
            Role.CUSTOMER
        );

        assertThrows(RuntimeException.class, () -> {
            authService.login(email, "4321");
        });
    }
    
    @Test
    void shouldFailIfUserDoesNotExist() {
        assertThrows(RuntimeException.class, () -> {
            authService.login("12@test.com", "password");
        });
    }


}
