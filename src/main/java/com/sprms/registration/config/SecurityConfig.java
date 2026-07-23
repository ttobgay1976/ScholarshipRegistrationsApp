package com.sprms.registration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http

				.csrf(csrf -> csrf.ignoringRequestMatchers("/webhook"))
				// Keep CSRF enabled
				.csrf(Customizer.withDefaults())

				.headers(
						headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'none';"))

								.addHeaderWriter((request, response) -> {
									response.setHeader("Cross-Origin-Opener-Policy", "same-origin-allow-popups");
								})

								.addHeaderWriter((request, response) -> {
									response.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");
								})

								.addHeaderWriter((request, response) -> {
									response.setHeader("Permissions-Policy",
											"geolocation=(), camera=(), microphone=(), payment=(), usb=()");
								}))

				.authorizeHttpRequests(auth -> auth

						// ==========================
						// Scholarship Public Module
						// ==========================

						.requestMatchers("/", "/ndi/login", "/ndi/events/**", "/ndi/post-login**", "/webhook**",
								"/scholarship/registrationfrm", "/scholarship/**", "/student/check-student-type/**",
								"/service/citizen/details**")
						.permitAll()

						// Static resources
						.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

						// Everything else requires login
						.anyRequest().authenticated());
		// .anyRequest().permitAll());

		return http.build();
	}
}
