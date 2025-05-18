package com.studentApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.dto.response.NotificationResponse;
import com.studentApp.entity.Notification;
import com.studentApp.entity.NotificationRecipient;
import com.studentApp.entity.Student;
import com.studentApp.entity.User;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.repository.NotificationRecipientRepository;
import com.studentApp.repository.NotificationRepository;
import com.studentApp.repository.StudentRepository;
import com.studentApp.repository.UserRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private NotificationRecipientRepository notificationRecipientRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentRepository studentRepository;

	// Gửi thông báo đến tất cả người dùng
	public Notification sendToAllUsers(String title, String content, User sender) {
		Notification notification = new Notification();
		notification.setTitle(title);
		notification.setContent(content);
		notification.setSender(sender);
		notification.setNotificationType("General");
		notification.setSentAt(LocalDateTime.now());

		notification = notificationRepository.save(notification);

		List<User> allUsers = userRepository.findAll();
		for (User user : allUsers) {
			NotificationRecipient recipient = new NotificationRecipient();
			recipient.setNotification(notification);
			recipient.setRecipient(user);
			recipient.setRead(false); // Mặc định là chưa đọc
			notificationRecipientRepository.save(recipient);
		}

		return notification;
	}

	// Gửi thông báo đến tất cả giáo viên
	public Notification sendToAllTeachers(String title, String content, User sender) {
		Notification notification = new Notification();
		notification.setTitle(title);
		notification.setContent(content);
		notification.setSender(sender);
		notification.setNotificationType("General");
		notification.setSentAt(LocalDateTime.now());

		notification = notificationRepository.save(notification);

		List<User> teachers = userRepository.findByRoleName("TEACHER");
		for (User teacher : teachers) {
			NotificationRecipient recipient = new NotificationRecipient();
			recipient.setNotification(notification);
			recipient.setRecipient(teacher);
			recipient.setRead(false);
			notificationRecipientRepository.save(recipient);
		}

		return notification;
	}

	// Gửi thông báo đến tất cả sinh viên
	public Notification sendToAllStudents(String title, String content, User sender) {
		Notification notification = new Notification();
		notification.setTitle(title);
		notification.setContent(content);
		notification.setSender(sender);
		notification.setNotificationType("General");
		notification.setSentAt(LocalDateTime.now());

		notification = notificationRepository.save(notification);

		List<User> students = userRepository.findByRoleName("STUDENT");
		for (User student : students) {
			NotificationRecipient recipient = new NotificationRecipient();
			recipient.setNotification(notification);
			recipient.setRecipient(student);
			recipient.setRead(false);
			notificationRecipientRepository.save(recipient);
		}

		return notification;
	}

	// Gửi thông báo đến cá nhân cụ thể
	public Notification sendToIndividual(Long recipientId, String title, String content, User sender) {
		User recipient = userRepository.findById(recipientId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		Notification notification = new Notification();
		notification.setTitle(title);
		notification.setContent(content);
		notification.setSender(sender);
		notification.setNotificationType("Personal");
		notification.setSentAt(LocalDateTime.now());

		notification = notificationRepository.save(notification);

		NotificationRecipient recipientRecord = new NotificationRecipient();
		recipientRecord.setNotification(notification);
		recipientRecord.setRecipient(recipient);
		recipientRecord.setRead(false);
		notificationRecipientRepository.save(recipientRecord);

		return notification;
	}

	// Lấy danh sách thông báo của một sinh viên theo studentId
	public List<NotificationResponse> getNotificationsByStudentId(Long studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		User user = student.getUser();
		if (user == null) {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}

		List<NotificationRecipient> recipients = notificationRecipientRepository.findByRecipient(user);
		return recipients.stream().map(recipient -> {
			Notification notification = recipient.getNotification();
			NotificationResponse response = new NotificationResponse();
			response.setId(notification.getId());
			response.setTitle(notification.getTitle());
			response.setContent(notification.getContent());
			response.setSentAt(notification.getSentAt());
			response.setRead(recipient.isRead());
			return response;
		}).collect(Collectors.toList());
	}

	// Đánh dấu thông báo là đã đọc
	public void markAsRead(Long notificationId, Long recipientId) {
		NotificationRecipient recipient = notificationRecipientRepository
				.findByNotificationIdAndRecipientId(notificationId, recipientId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		if (!recipient.isRead()) {
			recipient.setRead(true);
			notificationRecipientRepository.save(recipient);
		}
	}

	// Đếm số lượng thông báo chưa đọc của một sinh viên
	public long countUnreadNotificationsByStudentId(Long studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		User user = student.getUser();
		if (user == null) {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}

		return notificationRecipientRepository.countUnreadByRecipientId(user.getId());
	}
}