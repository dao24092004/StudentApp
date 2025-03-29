package com.studentApp.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenStorageService {

	private static final Logger logger = LoggerFactory.getLogger(TokenStorageService.class);

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void storeRefreshToken(String refreshToken, String userId, long expirationInMillis) {
		try {
			System.out.println("Storing refresh token in Redis: " + refreshToken + " for userId: " + userId);
			redisTemplate.opsForValue().set(refreshToken, userId, expirationInMillis, TimeUnit.MILLISECONDS);
			System.out.println("Stored refresh token in Redis: " + refreshToken);
		} catch (RedisConnectionFailureException e) {
			System.out.println("Failed to connect to Redis while storing refresh token: " + e.getMessage());
			throw new RuntimeException("Unable to connect to Redis", e);
		}
	}

	public String getUserIdFromRefreshToken(String refreshToken) {
		try {
			System.out.println("Retrieving userId for refresh token: " + refreshToken);
			String userId = redisTemplate.opsForValue().get(refreshToken);
			System.out.println("Retrieved userId: " + userId + " for refresh token: " + refreshToken);
			return userId;
		} catch (RedisConnectionFailureException e) {
			System.out.println("Failed to connect to Redis while retrieving refresh token: " + e.getMessage());
			return null;
		}
	}

	public void deleteRefreshToken(String refreshToken) {
		try {
			System.out.println("Deleting refresh token from Redis: " + refreshToken);
			redisTemplate.delete(refreshToken);
			System.out.println("Deleted refresh token from Redis: " + refreshToken);
		} catch (RedisConnectionFailureException e) {
			System.out.println("Failed to connect to Redis while deleting refresh token: " + e.getMessage());
		}
	}

	public void blacklistToken(String token, long expirationInMillis) {
		try {
			System.out.println(
					"Blacklisting token in Redis: " + token + " with expiration: " + expirationInMillis + "ms");
			redisTemplate.opsForValue().set(token, "blacklisted", expirationInMillis, TimeUnit.MILLISECONDS);
			System.out.println("Successfully blacklisted token in Redis: " + token);
		} catch (RedisConnectionFailureException e) {
			System.out.println("Failed to connect to Redis while blacklisting token: " + e.getMessage());
			throw new RuntimeException("Unable to connect to Redis", e);
		}
	}

	public boolean isTokenBlacklisted(String token) {
		try {
			System.out.println("Checking if token is blacklisted: " + token);
			boolean isBlacklisted = redisTemplate.hasKey(token);
			System.out.println("Token " + token + " is blacklisted: " + isBlacklisted);
			return isBlacklisted;
		} catch (RedisConnectionFailureException e) {
			System.out.println("Failed to connect to Redis while checking blacklisted token: " + e.getMessage());
			return false;
		}
	}

	// Phương thức giả định để lấy refresh token dựa trên username
	public String getRefreshTokenForUser(String username) {
		// Triển khai logic để lấy refresh token từ Redis hoặc cơ sở dữ liệu
		// Ví dụ: Lấy tất cả key trong Redis và tìm key có userId khớp với username
		System.out.println("Looking for refresh token for username: " + username);
		// Đây chỉ là giả định, bạn cần triển khai logic thực tế
		return null; // Thay bằng logic thực tế
	}
}