package com.studentApp.controller;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.studentApp.service.UserProfileService;

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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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
    private UserProfileService userProfileService;

    @PostMapping("/login")
    @Transactional
    @Operation(summary = "User login", description = "Authenticate user and return access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content) })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername().toLowerCase());
            if (user == null) {
                logger.warn("User not found for username: {}", loginRequest.getUsername());
                return ResponseEntity.status(404).body("User not found");
            }

            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = generateAndSaveRefreshToken(user);

            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, user));
        } catch (Exception e) {
            logger.error("Login failed for username: {}", loginRequest.getUsername(), e);
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
@Transactional
@Operation(summary = "User registration", description = "Register a new user and return access and refresh tokens")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "409", description = "Username or email already exists", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
})
public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    if (userRepository.existsByUsername(registerRequest.getUsername().toLowerCase())) {
        return ResponseEntity.status(409).body("Username already exists");
    }

    if (userRepository.existsByEmail(registerRequest.getEmail().toLowerCase())) {
        return ResponseEntity.status(409).body("Email already exists. Please use a different email or try logging in.");
    }

    User user = new User();
    user.setUsername(registerRequest.getUsername().toLowerCase());
    user.setEmail(registerRequest.getEmail().toLowerCase());
    user.setPassword(jwtService.encodePassword(registerRequest.getPassword()));

    Role userRole = roleRepository.findByRoleName("STUDENT")
            .orElseThrow(() -> new RuntimeException("Default role 'STUDENT' not found. Please contact admin."));
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
            logger.warn("Invalid token: Authorization header is missing or does not start with Bearer");
            return ResponseEntity.badRequest().body("Invalid token");
        }

        String accessToken = authHeader.substring(7);
        logger.info("Logging out with access token: {}", accessToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.warn("No authentication found in SecurityContext");
            return ResponseEntity.badRequest().body("User not authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        logger.info("Username from SecurityContext: {}", username);

        logger.info("Attempting to blacklist access token: {}", accessToken);
        tokenStorageService.blacklistToken(accessToken, jwtTokenProvider.getAccessTokenExpiration());
        logger.info("Access token blacklisted successfully: {}", accessToken);

        logger.info("Deleting refresh token from database for username: {}", username);
        refreshTokenRepository.deleteByUserId(user.getId());
        logger.info("Refresh token deleted from database for username: {}", username);

        String refreshToken = tokenStorageService.getRefreshTokenForUser(username);
        if (refreshToken != null) {
            logger.info("Deleting refresh token from Redis: {}", refreshToken);
            tokenStorageService.deleteRefreshToken(refreshToken);
            logger.info("Refresh token deleted from Redis: {}", refreshToken);
        } else {
            logger.info("No refresh token found in Redis for username: {}", username);
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
            logger.warn("Unauthorized access to profile");
            return ResponseEntity.status(401).body(null);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        try {
            UserProfileResponse response = userProfileService.getUserProfile(username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error retrieving user profile for username: {}", username, e);
            return ResponseEntity.status(e.getMessage().contains("not found") ? 404 : 500).body(null);
        }
    }

    @GetMapping("/check-email")
    @Operation(summary = "Check email availability", description = "Check if an email is already registered")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        if (userRepository.existsByEmail(email.toLowerCase())) {
            return ResponseEntity.status(409).body("Email already exists");
        }
        return ResponseEntity.ok("Email is available");
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