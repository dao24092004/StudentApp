package com.studentApp.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.studentApp.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.access-token-expiration}")
	private long accessTokenExpiration;

	@Value("${jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", user.getRole().getRoleName());
		claims.put("permissions",
				user.getRole().getPermissions().stream().map(permission -> permission.getPermissionName()).toList());

		return Jwts.builder().setClaims(claims).setSubject(user.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)).signWith(getSigningKey())
				.compact();
	}

	public String generateRefreshToken(User user) {
		return Jwts.builder().setSubject(user.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)).signWith(getSigningKey())
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
		return claims.getSubject();
	}

	public long getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public long getRefreshTokenExpiration() {
		return refreshTokenExpiration;
	}
}