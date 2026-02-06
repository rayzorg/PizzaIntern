package com.example.internproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.internproject.dto.ContactFormDto;
import com.example.internproject.services.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {

	private final EmailService service;

	public ContactController(EmailService service) {
		this.service = service;
	}
	
	@PostMapping
    public ResponseEntity<Map<String, String>> sendContact(@Valid @RequestBody ContactFormDto contactForm) {
		service.sendContactEmail(contactForm);
		 Map<String, String> response = new HashMap<>();
		    response.put("message", "Message sent successfully");

		    return ResponseEntity.ok(response);
    }
	
	
}
