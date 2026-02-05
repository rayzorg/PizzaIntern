package com.example.internproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.internproject.dto.LoginRequest;
import com.example.internproject.dto.LoginResponse;
import com.example.internproject.dto.RegisterRequest;
import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.services.AuthService;
import com.example.internproject.services.JwtService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class AuthController {

	private final AuthService authService;
	private final JwtService jwtService;

	public AuthController(AuthService authService, JwtService jwtService) {
		this.authService = authService;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
		String token = authService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(new LoginResponse(token));
	}

	@PostMapping("/register")
	public ResponseEntity<LoginResponse> register(@RequestBody @Valid RegisterRequest request) {
		User user = authService.register(request.getName(), request.getEmail(), request.getPassword(),
				request.getPhoneNumber(), Role.CUSTOMER);

		String token = jwtService.generateToken(user.getEmail(), user.getRole());
		return ResponseEntity.ok(new LoginResponse(token));
	}
}
