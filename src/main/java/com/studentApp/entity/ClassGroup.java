package com.studentApp.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_class_group")
@Data
public class ClassGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "group_code", nullable = false, unique = true, length = 20)
	private String groupCode;

	@Column(name = "group_name", nullable = false, length = 100)
	private String groupName;

	@ManyToOne
	@JoinColumn(name = "major_id", nullable = false)
	private Major major;

	@Column(length = 10)
	private String shift;

	@ManyToOne
	@JoinColumn(name = "semester_id", nullable = false)
	private Semester semester;

	@OneToMany(mappedBy = "classGroup", cascade = CascadeType.ALL)
	private List<Student> students;
}