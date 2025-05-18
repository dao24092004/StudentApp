package com.studentApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.NotificationRecipient;
import com.studentApp.entity.User;

@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Long> {
	List<NotificationRecipient> findByRecipient(User recipient);

	@Query("SELECT nr FROM NotificationRecipient nr WHERE nr.notification.id = :notificationId AND nr.recipient.id = :recipientId")
	Optional<NotificationRecipient> findByNotificationIdAndRecipientId(Long notificationId, Long recipientId);

	@Query("SELECT COUNT(nr) FROM NotificationRecipient nr WHERE nr.recipient.id = :recipientId AND nr.isRead = false")
	long countUnreadByRecipientId(Long recipientId);
}