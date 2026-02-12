package com.example.internproject.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.services.AuthService;
import com.example.internproject.services.JwtService;
import com.example.internproject.services.UserService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
public class CustomerSecurityTest {

	@Autowired
    private MockMvc mockMvc;
	
	
    
    
	
	@Test
	void shouldAllowAccessToCustomerOrdersEndpoint() throws Exception {
	   
	}
	@Test
	void shouldNotAllowAccessToCustomerOrdersEndpointIfAdmin() throws Exception {
	    

	}
	@Test
	void shouldNotAllowAccessToCustomerOrdersEndpointIfUnauthorized() throws Exception {
	    
	}
	@Test
	void givenCustomer_WhenPlaceOrder_ShouldCreateOrder() throws Exception {
	    
	}
	@Test
	void givenAdmin_WhenPlaceOrder_ThenAccessDenied() throws Exception {
	    
	}
	@Test
	void givenGuest_WhenPlaceOrder_ThenAccessDenied() throws Exception {
	    
	}
	
}