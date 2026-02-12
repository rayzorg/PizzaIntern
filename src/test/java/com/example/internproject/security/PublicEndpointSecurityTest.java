package com.example.internproject.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class PublicEndpointSecurityTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void everyoneShouldAccessPizzas() throws Exception {
		mockMvc.perform(get("/api/pizzas").with(anonymous())).andExpect(status().isOk());
	}

	@Test
	void everyoneShouldAccessToppings() throws Exception {
		mockMvc.perform(get("/api/toppings").with(anonymous())).andExpect(status().isOk());
	}

}
