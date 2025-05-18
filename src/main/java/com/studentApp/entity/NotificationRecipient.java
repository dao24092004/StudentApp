package com.studentApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_notification_recipient")
@Data
@Getter
@Setter
public class NotificationRecipient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "notification_id", nullable = false)
	private Notification notification;

	@ManyToOne
	@JoinColumn(name = "recipient_id", nullable = false)
	private User recipient;

	@Column(name = "is_read")
	private boolean isRead = false;
}