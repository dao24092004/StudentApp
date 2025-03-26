package com.studentApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.studentApp.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(
						exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setStatus(403);
							response.setContentType("application/json");
							response.getWriter().write(
									"{\"code\": 403, \"message\": \"Access Denied: You do not have the required permissions to access this resource.\"}");
						}))
				.authorizeHttpRequests(auth -> auth
						// Public endpoints
						.requestMatchers("/auth/login", "/auth/register").permitAll()
						// User management
						.requestMatchers("/api/users/**").hasAuthority("USER_VIEW").requestMatchers("/api/users/create")
						.hasAuthority("USER_CREATE").requestMatchers("/api/users/update/**").hasAuthority("USER_UPDATE")
						.requestMatchers("/api/users/delete/**").hasAuthority("USER_DELETE")
						// Class management
						.requestMatchers("/api/classes/**").hasAuthority("CLASS_VIEW")
						.requestMatchers("/api/classes/create").hasAuthority("CLASS_CREATE")
						.requestMatchers("/api/classes/update/**").hasAuthority("CLASS_UPDATE")
						.requestMatchers("/api/classes/delete/**").hasAuthority("CLASS_DELETE")
						// Subject management
						.requestMatchers("/api/subjects/**").hasAuthority("SUBJECT_VIEW")
						.requestMatchers("/api/subjects/create").hasAuthority("SUBJECT_CREATE")
						.requestMatchers("/api/subjects/update/**").hasAuthority("SUBJECT_UPDATE")
						.requestMatchers("/api/subjects/delete/**").hasAuthority("SUBJECT_DELETE")
						// Grade management
						.requestMatchers("/api/grades/**").hasAuthority("GRADE_VIEW")
						.requestMatchers("/api/grades/create").hasAuthority("GRADE_CREATE")
						.requestMatchers("/api/grades/update/**").hasAuthority("GRADE_UPDATE")
						.requestMatchers("/api/grades/delete/**").hasAuthority("GRADE_DELETE")
						// Schedule management
						.requestMatchers("/api/schedules/**").hasAuthority("SCHEDULE_VIEW")
						.requestMatchers("/api/schedules/create").hasAuthority("SCHEDULE_CREATE")
						.requestMatchers("/api/schedules/update/**").hasAuthority("SCHEDULE_UPDATE")
						.requestMatchers("/api/schedules/delete/**").hasAuthority("SCHEDULE_DELETE")
						// Notification management
						.requestMatchers("/api/notifications/**").hasAuthority("NOTIFICATION_VIEW")
						.requestMatchers("/api/notifications/create").hasAuthority("NOTIFICATION_CREATE")
						// Profile
						.requestMatchers("/api/profile/**").hasAuthority("PROFILE_VIEW")
						// Department management
						.requestMatchers("/admin/departments/**").hasAuthority("DEPARTMENT_VIEW")
						.requestMatchers("/admin/departments/insert").hasAuthority("DEPARTMENT_CREATE")
						.requestMatchers("/admin/departments/update/**").hasAuthority("DEPARTMENT_UPDATE")
						.requestMatchers("/admin/departments/delete/**").hasAuthority("DEPARTMENT_DELETE")
						// Permission management
						.requestMatchers("/admin/permissions").hasAuthority("PERMISSION_CREATE")
						.requestMatchers("/admin/permissions/{id}").hasAuthority("PERMISSION_UPDATE")
						.requestMatchers("/admin/permissions/{id}").hasAuthority("PERMISSION_DELETE")
						.requestMatchers("/admin/permissions/assign").hasAuthority("PERMISSION_ASSIGN")
						.requestMatchers("/admin/permissions/revoke").hasAuthority("PERMISSION_REVOKE")
						// Yêu cầu xác thực cho các endpoint khác
						.anyRequest().authenticated());

		// Thêm JWT filter
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}