package com.example.internproject.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;
import com.example.internproject.controller.ContactController;
import com.example.internproject.dto.ContactFormDto;
import com.example.internproject.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private EmailService emailService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void givenValidContactForm_whenPost_thenEmailIsSent() throws Exception {
		// given
		ContactFormDto dto = new ContactFormDto("rayan", "test@test.com", "question", "ggg", "094409",
				"test is nothing");
		// when then
		mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		then(emailService).should().sendContactEmail(argThat(actual -> actual.getName().equals("rayan")
				&& actual.getEmail().equals("test@test.com") && actual.getMessage().equals("test is nothing")));
	}

	@Test
	void givenInvalidEmail_whenPost_thenBadRequestAndEmailNotSent() throws Exception {
		// GIVEN
		ContactFormDto dto = new ContactFormDto("rayan", "testtest.com", "question", "ggg", "094409",
				"test is nothing");
		// WHEN + THEN
		mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());

		then(emailService).should(never()).sendContactEmail(any(ContactFormDto.class));
	}

	@Test
	void givenNoName_whenPost_thenBadRequestAndEmailNotSent() throws Exception {
		// GIVEN
		ContactFormDto dto = new ContactFormDto("", "test@test.com", "question", "ggg", "094409", "test is nothing");
		// WHEN + THEN
		mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());

		then(emailService).should(never()).sendContactEmail(any(ContactFormDto.class));
	}

}
