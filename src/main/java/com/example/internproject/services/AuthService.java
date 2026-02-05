package com.example.internproject.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.repository.UserRepository;
import com.example.internproject.security.UserPrincipal;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

	private final AuthenticationManager authManager;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public AuthService(AuthenticationManager authManager, JwtService jwtService,UserRepository userRepository,PasswordEncoder passwordEncoder) {
		this.authManager = authManager;
		this.jwtService = jwtService;
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
	}

	public String login(String email, String password) {
		Authentication auth;
		try {
			auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new RuntimeException("Invalid email or password");
		}

		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

		return jwtService.generateToken(principal.getUsername(), principal.getRole(),principal.getName());
	}

	public User register(String name, String email, String rawPassword, String phoneNumber, Role role) {
		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email is already taken");
		}
		User user = new User(name, phoneNumber, email, rawPassword, role);
		user.setPassword(passwordEncoder.encode(rawPassword));

		return userRepository.save(user);
	}
}
