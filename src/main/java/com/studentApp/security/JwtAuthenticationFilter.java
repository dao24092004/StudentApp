package com.studentApp.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.studentApp.service.TokenStorageService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	// Danh sách các đường dẫn không cần kiểm tra JWT
	private static final List<String> EXCLUDED_PATHS = Arrays.asList("/auth/login", "/auth/register",
			"/auth/refresh-token", "/auth/logout", "/swagger-ui.html", "/swagger-ui/**", "/api-docs/**",
			"/v3/api-docs/**", "/swagger-resources/**", "/webjars/**");

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenStorageService tokenStorageService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String path = request.getRequestURI();
			// Bỏ qua các đường dẫn không cần kiểm tra JWT
			if (EXCLUDED_PATHS.stream().anyMatch(path::equals)) {
				System.out.println("Bypassing JWT filter for path: " + path);
				filterChain.doFilter(request, response);
				return;
			}

			String jwt = getJwtFromRequest(request);
			if (jwt == null) {
				System.out.println("No JWT token found in request header");
			} else {
				System.out.println("JWT token found in request: " + jwt);
			}

			if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
				System.out.println("JWT token is valid: " + jwt);

				// Kiểm tra xem token có trong blacklist không
				boolean isBlacklisted = tokenStorageService.isTokenBlacklisted(jwt);
				System.out.println("Token " + jwt + " is blacklisted: " + isBlacklisted);
				if (isBlacklisted) {
					System.out.println("Token is blacklisted, rejecting request");
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token has been blacklisted");
					return;
				}

				String username = jwtTokenProvider.getUsernameFromToken(jwt);
				System.out.println("Username extracted from token: " + username);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				System.out.println("UserDetails loaded: " + userDetails.getUsername());

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
				System.out.println("Authentication set in SecurityContext for user: " + username);
			} else {
				System.out.println("JWT token is invalid or missing");
			}
		} catch (Exception ex) {
			System.out.println("Error in JwtAuthenticationFilter: " + ex.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}