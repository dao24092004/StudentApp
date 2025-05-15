package com.studentApp.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.LoginRequest;
import com.studentApp.dto.request.RegisterRequest;
import com.studentApp.dto.response.LoginResponse;
import com.studentApp.dto.response.UserProfileResponse;
import com.studentApp.entity.RefreshToken;
import com.studentApp.entity.Role;
import com.studentApp.entity.User;
import com.studentApp.repository.BlacklistTokenRepository;
import com.studentApp.repository.RefreshTokenRepository;
import com.studentApp.repository.RoleRepository;
import com.studentApp.repository.StudentRepository;
import com.studentApp.repository.UserRepository;
import com.studentApp.security.JwtTokenProvider;
import com.studentApp.service.JwtService;
import com.studentApp.service.TokenStorageService;
import com.studentApp.service.UserProfileService; // Thêm import

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and token management")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private BlacklistTokenRepository blacklistTokenRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TokenStorageService tokenStorageService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private UserProfileService userProfileService; // Thêm dependency

	@PostMapping("/login")
	@Transactional
	@Operation(summary = "User login", description = "Authenticate user and return access and refresh tokens")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content),
			@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content) })
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = userRepository.findByUsername(loginRequest.getUsername());
		if (user == null) {
			return ResponseEntity.status(404).body("User not found");
		}

		String accessToken = jwtTokenProvider.generateAccessToken(user);
		String refreshToken = generateAndSaveRefreshToken(user);

		return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, user));
	}

	@PostMapping("/register")
	@Transactional
	@Operation(summary = "User registration", description = "Register a new user and return access and refresh tokens")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "409", description = "Username or email already exists", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
		if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
			return ResponseEntity.status(409).body("Username already exists");
		}

		if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
			return ResponseEntity.status(409).body("Email already exists");
		}

		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(jwtService.encodePassword(registerRequest.getPassword()));

		Role userRole = roleRepository.findByRoleName("STUDENT")
				.orElseThrow(() -> new RuntimeException("Role not found"));
		if (userRole == null) {
			return ResponseEntity.status(500).body("Default role 'USER' not found. Please contact admin.");
		}
		user.setRole(userRole);

		userRepository.save(user);

		String accessToken = jwtTokenProvider.generateAccessToken(user);
		String refreshToken = generateAndSaveRefreshToken(user);

		return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, user));
	}

	@PostMapping("/refresh-token")
	@Operation(summary = "Refresh access token", description = "Generate a new access token using a refresh token")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "401", description = "Invalid or expired refresh token", content = @Content) })
	public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
		String userId = tokenStorageService.getUserIdFromRefreshToken(refreshToken);
		if (userId != null) {
			User user = userRepository.findById(Long.parseLong(userId))
					.orElseThrow(() -> new RuntimeException("User not found"));
			String newAccessToken = jwtTokenProvider.generateAccessToken(user);
			return ResponseEntity.ok(new LoginResponse(newAccessToken, refreshToken, user));
		} else {
			return ResponseEntity.status(401).body("Invalid or expired refresh token");
		}
	}

	@PostMapping("/logout")
	@Transactional
	@Operation(summary = "User logout", description = "Log out user by blacklisting access token and deleting refresh token")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logged out successfully", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid token or user not authenticated", content = @Content) })
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("Invalid token: Authorization header is missing or does not start with Bearer");
			return ResponseEntity.badRequest().body("Invalid token");
		}

		String accessToken = authHeader.substring(7);
		System.out.println("Logging out with access token: " + accessToken);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			System.out.println("No authentication found in SecurityContext");
			return ResponseEntity.badRequest().body("User not authenticated");
		}
		String username = authentication.getName();
		User user = userRepository.findByUsername(username);
		System.out.println("Username from SecurityContext: " + username);

		System.out.println("Attempting to blacklist access token: " + accessToken);
		tokenStorageService.blacklistToken(accessToken, jwtTokenProvider.getAccessTokenExpiration());
		System.out.println("Access token blacklisted successfully: " + accessToken);

		System.out.println("Deleting refresh token from database for username: " + username);
		refreshTokenRepository.deleteByUserId(user.getId());
		System.out.println("Refresh token deleted from database for username: " + username);

		String refreshToken = tokenStorageService.getRefreshTokenForUser(username);
		if (refreshToken != null) {
			System.out.println("Deleting refresh token from Redis: " + refreshToken);
			tokenStorageService.deleteRefreshToken(refreshToken);
			System.out.println("Refresh token deleted from Redis: " + refreshToken);
		} else {
			System.out.println("No refresh token found in Redis for username: " + username);
		}

		return ResponseEntity.ok("Logged out successfully");
	}

	@GetMapping("/profile")
	@Operation(summary = "Get user profile", description = "Retrieve the authenticated user's profile information")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content) })
	public ResponseEntity<UserProfileResponse> getProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			return ResponseEntity.status(401).body(null);
		}

		// Lấy username từ UserDetails
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();

		// Sử dụng UserProfileService để lấy thông tin hồ sơ
		try {
			UserProfileResponse response = userProfileService.getUserProfile(username);
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(404).body(null);
		}
	}

	private String generateAndSaveRefreshToken(User user) {
		refreshTokenRepository.deleteByUserId(user.getId());

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiryDate(Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenExpiration()));
		refreshToken.setUser(user);
		refreshTokenRepository.save(refreshToken);

		tokenStorageService.storeRefreshToken(refreshToken.getToken(), user.getId().toString(),
				jwtTokenProvider.getRefreshTokenExpiration());

		return refreshToken.getToken();
	}
}