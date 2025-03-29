package com.studentApp.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.entity.RefreshToken;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private JwtService jwtService;

	@Transactional
	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUserId(userId);
		refreshToken.setToken(jwtService.generateRefreshToken(null));
		refreshToken.setExpiryDate(Instant.now().plusMillis(jwtService.getRefreshTokenExpiration()));
		refreshToken.setCreatedAt(LocalDateTime.now());
		return refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken verifyRefreshToken(String token) {
		Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
		RefreshToken refreshToken = refreshTokenOpt
				.orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

		if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(refreshToken);
			throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		}

		return refreshToken;
	}

	@Transactional
	public void deleteByUserId(Long userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}
}