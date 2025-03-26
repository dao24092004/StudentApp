package com.studentApp.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpiration);

		// Lấy danh sách roles và permissions từ authorities
		List<String> roles = authentication.getAuthorities().stream()
				.filter(auth -> auth.getAuthority().startsWith("ROLE_")).map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		List<String> permissions = authentication.getAuthorities().stream()
				.filter(auth -> !auth.getAuthority().startsWith("ROLE_")).map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		// Thêm roles và permissions vào claims
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", roles);
		claims.put("permissions", permissions);

		return Jwts.builder().subject(username).claims(claims).issuedAt(now).expiration(expiryDate)
				.signWith(getSigningKey()).compact();
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
		return claims.getSubject();
	}

	@SuppressWarnings("unchecked")
	public List<String> getRolesFromToken(String token) {
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
		return (List<String>) claims.get("roles");
	}

	@SuppressWarnings("unchecked")
	public List<String> getPermissionsFromToken(String token) {
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
		return (List<String>) claims.get("permissions");
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			throw new AppException(ErrorCode.TOKEN_EXPIRED);
		} catch (MalformedJwtException e) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		} catch (Exception e) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}
	}
}