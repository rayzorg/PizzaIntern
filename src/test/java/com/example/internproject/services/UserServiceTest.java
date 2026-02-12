package com.example.internproject.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.internproject.models.User;
import com.example.internproject.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserService userService;

	@Test
	void givenAUser_ReturnCorrectNameBasedOnEmail() {
		// GIVEN
		User user = new User();
		user.setEmail("test@example.com");
		user.setName("Rayan");

		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		// WHEN
		User result = userService.loadUserByEmail("test@example.com");

		// THEN
		assertEquals("Rayan", result.getName());
	}

	@Test
	void givenNonExistingUser_ThrowsUsernameNotFoundException() {
		// GIVEN
		when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

		// WHEN + THEN
		assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByEmail("unknown@example.com"));
	}

	@Test
	void givenAUser_CheckIfUserPrincipalContainsCorrectEmail() {
		// GIVEN
		User user = new User();
		user.setEmail("test@example.com");
		user.setName("Rayan");

		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		// WHEN
		var principal = userService.loadUserByUsername("test@example.com");

		// THEN
		assertEquals(user.getEmail(), principal.getUsername());
	}
}
