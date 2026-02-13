package com.example.internproject.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.repository.UserRepository;
import com.example.internproject.security.UserPrincipal;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

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

		when(userRepository.existsByEmail(email)).thenReturn(false); // user does not exist
		when(passwordEncoder.encode(password)).thenReturn("hashedSecret");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		// when
		User user = authService.register("John Doe", "john@test.com", "secret123", "123456789", Role.CUSTOMER);
		// then
		assertNotNull(user);
		verify(userRepository).save(user);

	}

	@Test
	void shouldFailRegisteringIfEmailAlreadyExists() {

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
	void shouldFailIfCredentialsAreInvalidWhenLogging() {

		// GIVEN
		String email = "12@test.com";

		when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

		// WHEN THEN
		BadCredentialsException exception = assertThrows(BadCredentialsException.class,
				() -> authService.login(email, "password"));

		assertEquals("Invalid email or password", exception.getMessage());

		verify(authManager).authenticate(any());
	}

	@Test
	void shouldLoginSuccessfullyWithEmailAndPassword() {

		// given
		User user = new User("rayan", "0493933", "rayan@rayan.com", "hashedpassword", Role.CUSTOMER);
		UserPrincipal principal = new UserPrincipal(user);

		Authentication authentication = mock(Authentication.class);

		when(authentication.getPrincipal()).thenReturn(principal);
		when(authManager.authenticate(any())).thenReturn(authentication);
		when(jwtService.generateToken("rayan@rayan.com", Role.CUSTOMER, "rayan")).thenReturn("jwt-token");
		// when
		String token = authService.login(user.getEmail(), "password");

		// then
		assertEquals("jwt-token", token);

		verify(authManager).authenticate(any());
		verify(jwtService).generateToken(user.getEmail(), Role.CUSTOMER, user.getName());
	}

}
