package com.example.internproject.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.internproject.models.Role;
import com.example.internproject.models.User;
import com.example.internproject.repository.UserRepository;
import com.example.internproject.security.UserPrincipal;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String name, String email, String rawPassword,String phoneNumber,Role role) {
    	if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already taken"); 
        }
        User user = new User(name,phoneNumber,email,rawPassword,role);
        user.setPassword(passwordEncoder.encode(rawPassword));

       return userRepository.save(user);
    }

	
	public User loadUserByEmail(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
                .orElseThrow(() ->
                new RuntimeException("User not found")
                );
    }

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 User user = userRepository.findByEmail(email)
		            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

		        return new UserPrincipal(user);
	}
}

