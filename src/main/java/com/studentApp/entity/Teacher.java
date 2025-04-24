package com.studentApp.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tbl_teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
	public Teacher(Long teacherId) {
		this.id = teacherId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", unique = true)
	@JsonManagedReference
	private User user;

	@Column(name = "teacher_code", nullable = false, unique = true, length = 20)
	private String teacherCode;

	@Column(name = "teacher_name", nullable = false, length = 100)
	private String teacherName;

	@Column(name = "date_of_birth")
	private Date teacherDateOfBirth;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	public enum Gender {
		Male, Female;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 10)
	private Gender teacherGender;

	@Column(name = "address", length = 100)
	private String teacherAddress;

	@Column(name = "phone_number", unique = true, length = 15)
	private String teacherPhoneNumber;

	@Column(name = "email", unique = true, nullable = false, length = 100)
	private String teacherEmail;

	@ManyToOne(optional = false, cascade = CascadeType.MERGE)
	@JoinColumn(name = "dept_id", nullable = false)
	private Department department;
}