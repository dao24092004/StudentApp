package com.studentApp.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studentApp.enums.Gender;

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
@Table(name = "tbl_student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "user_id", unique = true)
	@JsonManagedReference
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
	@JoinColumn(name = "major_id", nullable = false)
	private Major major;

	@ManyToOne
    @JoinColumn(name = "class_group_id")
    private ClassGroup classGroup;
}