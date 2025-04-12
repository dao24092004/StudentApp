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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_teacher")
@Data
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@JoinColumn(name = "user_id", unique = true)
	private User user;

	@Column(name = "teacher_code", unique = true, nullable = false, length = 20)
	private String teacherCode;

	@Column(name = "teacher_name", nullable = false, length = 100)
	private String teacherName;

	@Column(name = "date_of_b")
	private Date dateOfBirth;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 10)
	private Gender gender;

	private enum Gender {
		Male, Famale;
	}

	@Column(name = "address", length = 100)
	private String address;

	@Column(name = "phone_number", length = 15, unique = true)
	private String phoneNumber;

	@Column(name = "email", nullable = false, length = 100)
	private String email;
}
