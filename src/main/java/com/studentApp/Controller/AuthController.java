package com.studentApp.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.LoginRequest;
import com.studentApp.dto.response.LoginResponse;
import com.studentApp.entity.RefreshToken;
import com.studentApp.entity.User;
import com.studentApp.repository.BlacklistTokenRepository;
import com.studentApp.repository.RefreshTokenRepository;
import com.studentApp.repository.UserRepository;
import com.studentApp.security.JwtTokenProvider;
import com.studentApp.service.JwtService;
import com.studentApp.service.TokenStorageService;

@RestController
@RequestMapping("/auth")
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
	private TokenStorageService tokenStorageService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/login")
	@Transactional // Thêm @Transactional để đảm bảo giao dịch
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

	@PostMapping("/refresh-token")
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
	@Transactional // Thêm @Transactional để đảm bảo giao dịch
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		// Kiểm tra header Authorization
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("Invalid token: Authorization header is missing or does not start with Bearer");
			return ResponseEntity.badRequest().body("Invalid token");
		}

		// Lấy access token từ header
		String accessToken = authHeader.substring(7);
		System.out.println("Logging out with access token: " + accessToken);

		// Lấy thông tin người dùng từ SecurityContext
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			System.out.println("No authentication found in SecurityContext");
			return ResponseEntity.badRequest().body("User not authenticated");
		}
		String username = authentication.getName();
		User user = userRepository.findByUsername(username);
		System.out.println("Username from SecurityContext: " + username);

		// Blacklist access token
		System.out.println("Attempting to blacklist access token: " + accessToken);
		tokenStorageService.blacklistToken(accessToken, jwtTokenProvider.getAccessTokenExpiration());
		System.out.println("Access token blacklisted successfully: " + accessToken);

		// Delete refresh token from database
		System.out.println("Deleting refresh token from database for username: " + username);
		refreshTokenRepository.deleteByUserId(user.getId());
		System.out.println("Refresh token deleted from database for username: " + username);

		// Xóa refresh token từ Redis
		// Lấy refresh token từ Redis hoặc cơ sở dữ liệu (cần triển khai logic lấy
		// refresh token)
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

	private String generateAndSaveRefreshToken(User user) {
		// Xóa refresh token cũ của user (nếu có)
		refreshTokenRepository.deleteByUserId(user.getId());

		// Tạo refresh token mới
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiryDate(Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenExpiration()));
		refreshToken.setUser(user);
		refreshTokenRepository.save(refreshToken);

		// Lưu vào Redis
		tokenStorageService.storeRefreshToken(refreshToken.getToken(), user.getId().toString(),
				jwtTokenProvider.getRefreshTokenExpiration());

		return refreshToken.getToken();
	}
}