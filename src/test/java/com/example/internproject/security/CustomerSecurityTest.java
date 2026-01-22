package com.example.internproject.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.services.JwtService;
import com.example.internproject.services.UserService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerSecurityTest {

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
    
    
	@BeforeEach
    void setup() {
        User admin = userService.register("Admin", "admin@test.com", "password", "000", Role.ADMIN);
        User customer = userService.register("Cust", "cust@test.com", "password", "111", Role.CUSTOMER);

        adminId = admin.getId();
        customerId = customer.getId();

        adminToken = jwtService.generateToken(admin.getEmail(), admin.getRole());
        customerToken = jwtService.generateToken(customer.getEmail(), customer.getRole());
        
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) 
                .build();
    }
	
	@Test
	void shouldAllowAccessToCustomerOrdersEndpoint() throws Exception {
	    mockMvc.perform(get("/api/orders/myorders")
	                    .header("Authorization", "Bearer " + customerToken))
	            .andExpect(status().isOk());
	}
	@Test
	void shouldNotAllowAccessToCustomerOrdersEndpointIfAdmin() throws Exception {
	    mockMvc.perform(get("/api/orders/myorders")
	    .header("Authorization", "Bearer " + adminToken))        
	            .andExpect(status().isForbidden());
        //throws 500 instead of 403          

	}
	@Test
	void shouldNotAllowAccessToCustomerOrdersEndpointIfUnauthorized() throws Exception {
	    mockMvc.perform(get("/api/orders/myorders"))
        //throws 500 instead of 403          
	            .andExpect(status().isUnauthorized());
	}
}