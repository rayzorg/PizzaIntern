package com.example.internproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
	
    @NotBlank(message="Name is required")

	private String name;
	@NotBlank(message="Email is required")
    @Email(message="Email must be valid")
	private String email;
	
	
	private String phoneNumber;
    @NotBlank(message="Password is required")

	private String password;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPassword() {
		return password;
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

	public void setPassword(String password) {
		this.password = password;
	}
	

	

}
