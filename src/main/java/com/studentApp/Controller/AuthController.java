package com.studentApp.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.Entity.Role;
import com.studentApp.Entity.User;
import com.studentApp.Repository.RoleRepository;
import com.studentApp.Repository.UserRepository;
import com.studentApp.dto.request.LoginRequest;
import com.studentApp.dto.request.RegisterRequest;
import com.studentApp.dto.response.LoginResponse;
import com.studentApp.dto.response.UserResponse;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.mapper.UserMapper;
import com.studentApp.security.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		String token = tokenProvider.generateToken(authentication);
		User user = userRepository.findByUsername(request.getUsername());
		if (user == null) {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}
		UserResponse userResponse = userMapper.toUserResponse(user);
		LoginResponse loginResponse = new LoginResponse(token);
		loginResponse.setUser(userResponse);
		return ResponseEntity.ok(loginResponse);
	}

	@GetMapping("/login")
	public ResponseEntity<String> loginNotSupported() {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body("GET method not supported for /auth/login. Use POST /auth/login with credentials.");
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
		if (userRepository.findByUsername(request.getUsername()) != null) {
			throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
		}
		if (userRepository.findByEmail(request.getEmail()) != null) {
			throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		Role role = roleRepository.findById(request.getRoleId())
				.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		user.setRole(role);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		userRepository.save(user);

		UserResponse userResponse = userMapper.toUserResponse(user);
		return ResponseEntity.ok(userResponse);
	}

	@GetMapping("/login/success")
	public ResponseEntity<LoginResponse> loginSuccess(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		String email = null;
		String username = null;

		if (principal instanceof OidcUser) {
			OidcUser oidcUser = (OidcUser) principal;
			email = oidcUser.getEmail();
			username = oidcUser.getPreferredUsername();
		} else if (principal instanceof OAuth2User) {
			OAuth2User oauth2User = (OAuth2User) principal;
			email = oauth2User.getAttribute("email");
			username = oauth2User.getAttribute("name");
		}

		if (email == null || username == null) {
			throw new AppException(ErrorCode.INVALID_OAUTH2_USER);
		}

		User user = userRepository.findByUsername(username);
		if (user == null) {
			user = new User();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode("social-login-" + username));
			Role defaultRole = roleRepository.findById(2L)
					.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
			user.setRole(defaultRole);
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdatedAt(LocalDateTime.now());
			userRepository.save(user);
		}

		Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authentication.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		String token = tokenProvider.generateToken(auth);

		UserResponse userResponse = userMapper.toUserResponse(user);
		LoginResponse loginResponse = new LoginResponse(token);
		loginResponse.setUser(userResponse);
		return ResponseEntity.ok(loginResponse);
	}
}