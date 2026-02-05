package com.example.internproject.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.services.AuthService;
import com.example.internproject.services.JwtService;
import com.example.internproject.services.UserService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminSecurityTests {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    private String adminToken;
    private String customerToken;

    private Long adminId;
    private Long customerId;
    
    
	@BeforeEach
    void setup() {
        User admin = authService.register("Admin", "admin@test.com", "password", "000", Role.ADMIN);
        User customer = authService.register("Cust", "cust@test.com", "password", "111", Role.CUSTOMER);

        adminId = admin.getId();
        customerId = customer.getId();

        adminToken = jwtService.generateToken(admin.getEmail(), admin.getRole(),admin.getName());
        customerToken = jwtService.generateToken(customer.getEmail(), customer.getRole(),customer.getName());
    }
	
	@Test
    void shouldAllowAccessToAdminOrderEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/orders")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
	
	
	@Test
    void shouldAllowAccessToAdminPizzaEndpoint() throws Exception {
		mockMvc.perform(put("/api/admin/pizzas/2/availability")
                .header("Authorization", "Bearer " + adminToken)
                .param("available", "true"))
        .andExpect(status().isOk());
    }
	@Test
    void shouldNotAllowAccessToAdminEndpointIfCustomer() throws Exception {
        mockMvc.perform(get("/api/admin/orders")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }
	@Test
    void shouldNotAllowAccessToAdminPizzaEndpointIfCustomer() throws Exception {
		mockMvc.perform(put("/api/admin/pizzas/2/availability")
                .header("Authorization", "Bearer " + customerToken)
                .param("available", "true"))
        .andExpect(status().isForbidden());
    }
	@Test
    void shouldNotAllowAccessToAdminEndpointIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/admin/orders"))
                .andExpect(status().isForbidden());
    }
	@Test
	void shouldNotAllowAccessToAdminPizzaEndpointIfUnauthenticated() throws Exception {
		mockMvc.perform(put("/api/admin/pizzas/2/availability")
                .param("available", "true"))
        .andExpect(status().isForbidden());
    }
}
