package com.studentApp.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_refresh_token")
@Data
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private Instant expiryDate;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// Getter và Setter cho userId (không cần thiết cho Spring Data JPA, nhưng có
	// thể giữ lại nếu cần)
	public Long getUserId() {
		return user != null ? user.getId() : null;
	}

	public void setUserId(Long userId) {
		if (this.user == null) {
			this.user = new User();
		}
		this.user.setId(userId);
	}
}