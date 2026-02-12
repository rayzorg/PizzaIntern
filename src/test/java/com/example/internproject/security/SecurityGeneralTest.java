package com.example.internproject.security;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.services.JwtService;
import com.example.internproject.services.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class SecurityGeneralTest {
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    private String adminToken;
    private String customerToken;

    private Long adminId;
    private Long customerId;
	
	
	  @Test
	    void shouldAllowAccessToPizzas() throws Exception {
	        mockMvc.perform(get("/api/pizzas"))
	                .andExpect(status().isOk());
	    }
	  @Test
	    void ShouldAllowAccessToToppings() throws Exception {
	        mockMvc.perform(get("/api/toppings"))
	                .andExpect(status().isOk());
	    }
	  
}
