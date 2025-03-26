package com.studentApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.studentApp.entity.Permission;
import com.studentApp.entity.User;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new AppException(ErrorCode.USER_NOT_FOUND, "User not found with username: " + username);
		}

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		// Thêm role vào authorities
		String roleName = user.getRole().getRoleName();
		if (roleName == null) {
			throw new AppException(ErrorCode.ROLE_NOT_FOUND, "Role not found for user: " + username);
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));

		// Thêm các permission vào authorities
		for (Permission permission : user.getRole().getPermissions()) {
			authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}
}