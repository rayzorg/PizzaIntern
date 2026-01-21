package com.example.internproject.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.internproject.services.JwtService;
import com.example.internproject.services.UserService;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfigPw {

	 private final JwtService jwtService;
	    private final UserService appUserService;

	    
	    
	 public SecurityConfigPw(JwtService jwtService, UserService appUserService) {
			this.jwtService = jwtService;
			this.appUserService = appUserService;
		}
	 
	 @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		 
		 
	        return config.getAuthenticationManager();
	    }
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
		 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
         .csrf(csrf -> csrf.disable())
         .authorizeHttpRequests(auth -> auth
        		 .requestMatchers("/api/auth/**").permitAll()
                 .requestMatchers("/api/pizzas/**").permitAll()
        		 .requestMatchers("/api/toppings/**").permitAll()
        		 .requestMatchers("/api/orders/**").permitAll()



                 
                 .requestMatchers("/api/admin/**").hasRole("ADMIN")

                 .anyRequest().authenticated()

             )
             .addFilterBefore(new JwtAuthenticationFilter(jwtService, appUserService), UsernamePasswordAuthenticationFilter.class);
         return http.build();
    }
	
	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration config = new CorsConfiguration();

	        config.setAllowedOrigins(List.of("http://localhost:4200"));
	        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(List.of("*"));
	        config.setAllowCredentials(true);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);

	        return source;
	    }
}
