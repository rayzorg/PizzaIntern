package com.example.internproject.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.repository.UserRepository;
import com.example.internproject.security.UserPrincipal;

import jakarta.transaction.Transactional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;
	@InjectMocks
    private AuthService authService;

	
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthenticationManager authManager;
	@Mock
	private JwtService jwtService;

	@Test
	void shouldRegisterUserSuccessfully() {
		// given
		String email = "john@test.com";
		String password = "secret123";

		when(userRepository.existsByEmail(email)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn("hashedSecret");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		// when
		User user = authService.register("John Doe", "john@test.com", "secret123", "123456789", Role.CUSTOMER);
		// then
		assertNotNull(user);
		verify(userRepository).save(user);

	}

	@Test
	void shouldFailIfEmailAlreadyExists() {

		// given
		String email = "duplicate@test.com";
		when(userRepository.existsByEmail(email)).thenReturn(true);
		// when,then
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> authService.register("John", email, "password", "123", Role.CUSTOMER));

		assertEquals("Email is already taken", exception.getMessage());
		verify(userRepository, never()).save(any());
	}

	@Test
	void shouldFailIfUserDoesNotExist() {

		// given
		String email = "12@test.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// when,then
		RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(email, "password"));

		assertEquals("User not found", exception.getMessage());
		verify(userRepository).findByEmail(email);
	}

	@Test
	void shouldFailIfCredentialsAreInvalid() {

		// given
		when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

		// when,then
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> authService.login("test@test.com", "wrong"));
		assertEquals("Invalid email or password", exception.getMessage());
	}

	@Test
	void shouldLoginSuccessfully() {

		// given
		User user = new User("rayan", "0493933", "rayan@rayan", "hashedpassword", Role.CUSTOMER);
		UserPrincipal principal = new UserPrincipal(user);

		Authentication authentication = mock(Authentication.class);

		when(authentication.getPrincipal()).thenReturn(principal);
		when(authManager.authenticate(any())).thenReturn(authentication);
		when(jwtService.generateToken("test@test.com", Role.CUSTOMER)).thenReturn("jwt-token");
		// when
		String token = authService.login(user.getEmail(), "password");

		// then
		assertEquals("jwt-token", token);

		verify(authManager).authenticate(any());
		verify(jwtService).generateToken(user.getEmail(), Role.CUSTOMER);
	}

}
