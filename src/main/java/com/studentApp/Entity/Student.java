package com.studentApp.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_student") // Tên bảng là tbl_student, không phải tbl_sinh_vien
@Data
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", unique = true) // Khóa ngoại tới tbl_user
	private User user;

	@Column(name = "student_code", nullable = false, unique = true, length = 20)
	private String studentCode;

	@Column(name = "student_name", nullable = false, length = 100)
	private String studentName;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 10)
	private Gender gender;

	@Column(name = "address", length = 100)
	private String address;

	@Column(name = "phone_number", unique = true, length = 15)
	private String phoneNumber;

	@ManyToOne
	@JoinColumn(name = "major_id", nullable = false) // Khóa ngoại tới tbl_major
	private Major major;

	// Enum để định nghĩa các giá trị hợp lệ của gender
	public enum Gender {
		Male, Female
	}
}