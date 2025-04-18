package com.studentApp.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@Column(name = "avatar_url", length = 255)
	private String avatarUrl;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updatedAt;

	@OneToOne(mappedBy = "user")
	@JsonIgnore // Changed from @JsonBackReference to @JsonIgnore to prevent serialization
	private Teacher teacher;

	@Override
	@JsonIgnore

	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (role == null) {
			return Collections.emptyList();
		}
		Set<GrantedAuthority> authorities = new HashSet<>();
		// Add the role as an authority
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		// Grant additional authorities based on the role
		if ("ADMIN".equals(role.getRoleName())) {
			// Admin has all user-related permissions, as per tbl_role_permission
			authorities.add(new SimpleGrantedAuthority("USER_CREATE"));
			authorities.add(new SimpleGrantedAuthority("USER_UPDATE"));
			authorities.add(new SimpleGrantedAuthority("USER_DELETE"));
			authorities.add(new SimpleGrantedAuthority("USER_VIEW"));
			// Add other permissions as needed (e.g., CLASS_CREATE, etc.)
		}
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
}