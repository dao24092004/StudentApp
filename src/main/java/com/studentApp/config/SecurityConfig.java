package com.studentApp.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.studentApp.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(
						exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setStatus(403);
							response.setContentType("application/json");
							response.getWriter().write(
									"{\"code\": 403, \"message\": \"Access Denied: You do not have the required permissions to access this resource.\"}");
						}).authenticationEntryPoint((request, response, authException) -> {
							response.setStatus(401);
							response.setContentType("application/json");
							response.getWriter()
									.write("{\"code\": 401, \"message\": \"Unauthorized: Invalid or missing token.\"}");
						}))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/actuator/health").permitAll().requestMatchers("/api/users/**")
						.hasAuthority("USER_VIEW").requestMatchers("/api/users/create").hasAuthority("USER_CREATE")
						.requestMatchers("/api/users/update/**").hasAuthority("USER_UPDATE")
						.requestMatchers("/api/users/delete/**").hasAuthority("USER_DELETE")
						.requestMatchers("/api/classes/department-subjects")
						.hasAnyAuthority("TEACHER_VIEW", "CLASS_VIEW")
						.requestMatchers("/api/classes/teachers/{teacherId}/subjects").hasAuthority("TEACHER_VIEW")
						.requestMatchers("/api/classes/teachers/register-subjects").hasAuthority("TEACHER_REGISTER")
						.requestMatchers("/api/classes/assign-classes").hasAuthority("CLASS_ASSIGN")
						.requestMatchers("/api/classes/schedules/generate").hasAuthority("SCHEDULE_CREATE")
						.requestMatchers("/api/classes/create/schedules").hasAuthority("CLASS_CREATE")
						.requestMatchers("/api/classes/update/schedules/**").hasAuthority("CLASS_UPDATE")
						.requestMatchers("/api/classes/delete/schedules/**").hasAuthority("CLASS_DELETE")
						.requestMatchers("/api/classes/schedules/week")
						.hasAnyAuthority("CLASS_VIEW", "TEACHER_VIEW", "STUDENT_VIEW")
						.requestMatchers("/api/classes/schedules/day")
						.hasAnyAuthority("CLASS_VIEW", "TEACHER_VIEW", "STUDENT_VIEW")
						.requestMatchers("/api/classes/schedules/class/**").hasAuthority("CLASS_VIEW")
						.requestMatchers("/api/subjects/**").hasAuthority("SUBJECT_VIEW")
						.requestMatchers("/api/subjects/create").hasAuthority("SUBJECT_CREATE")
						.requestMatchers("/api/subjects/update/**").hasAuthority("SUBJECT_UPDATE")
						.requestMatchers("/api/subjects/delete/**").hasAuthority("SUBJECT_DELETE")
						.requestMatchers("/api/grades/**").hasAuthority("GRADE_VIEW")
						.requestMatchers("/api/grades/create").hasAuthority("GRADE_CREATE")
						.requestMatchers("/api/grades/update/**").hasAuthority("GRADE_UPDATE")
						.requestMatchers("/api/grades/delete/**").hasAuthority("GRADE_DELETE")
						.requestMatchers("/api/notifications/**").hasAuthority("NOTIFICATION_VIEW")
						.requestMatchers("/api/notifications/create").hasAuthority("NOTIFICATION_CREATE")
						.requestMatchers("/api/profile/**").hasAuthority("PROFILE_VIEW")
						.requestMatchers("/admin/departments/**").hasAuthority("DEPARTMENT_VIEW")
						.requestMatchers("/admin/departments/insert").hasAuthority("DEPARTMENT_CREATE")
						.requestMatchers("/admin/departments/update/**").hasAuthority("DEPARTMENT_UPDATE")
						.requestMatchers("/admin/departments/delete/**").hasAuthority("DEPARTMENT_DELETE")
						.requestMatchers("/admin/permissions/").hasAuthority("PERMISSION_CREATE")
						.requestMatchers("/admin/permissions/view/**").hasAuthority("PERMISSION_VIEW")
						.requestMatchers("/admin/permissions/{id}").hasAuthority("PERMISSION_UPDATE")
						.requestMatchers("/admin/permissions/{id}").hasAuthority("PERMISSION_DELETE")
						.requestMatchers("/admin/permissions/assign").hasAuthority("PERMISSION_ASSIGN")
						.requestMatchers("/admin/permissions/revoke").hasAuthority("PERMISSION_REVOKE")
						.requestMatchers("/api/classes/registration/**").hasAuthority("STUDENT_REGISTER")
						.requestMatchers("/api/classes/import/**").hasAuthority("CLASS_CREATE")
						.requestMatchers("/api/classes/data-generator**").hasAuthority("CLASS_CREATE").anyRequest()
						.authenticated());

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*")); // Cho phép tất cả nguồn trong môi trường phát triển
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**",
				"/v3/api-docs/**", "/swagger-resources/**", "/webjars/**");
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