package com.example.internproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactFormDto {
	@NotBlank(message = "Name is required")
	private String name;
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;
	@NotBlank(message = "subject is required")
	private String subject;
	@NotBlank(message = "id is required")
	private String id;
	@NotBlank(message = "Phone is required")
	private String phoneNumber;
	@NotBlank(message = "Message cannot be emty")
	private String message;

	public ContactFormDto(@NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
			@NotBlank(message = "subject is required") String subject, @NotBlank(message = "id is required") String id,
			@NotBlank(message = "Phone is required") String phoneNumber,
			@NotBlank(message = "Message cannot be emty") String message) {
		this.name = name;
		this.email = email;
		this.subject = subject;
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
