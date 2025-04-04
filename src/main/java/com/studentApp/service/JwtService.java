package com.studentApp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
}