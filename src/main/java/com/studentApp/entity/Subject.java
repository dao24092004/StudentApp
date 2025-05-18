package com.studentApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tbl_subject", uniqueConstraints = { @UniqueConstraint(columnNames = { "subject_code" }) })
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "subject_code", nullable = false)
	private String subjectCode;

	@Column(name = "subject_name", nullable = false)
	private String subjectName;

	@Column(name = "credits", nullable = false)
	private Integer credits;

	@Column(name = "description")
	private String description;

	@Column(name = "dept_id", nullable = false)
	private Long deptId;

	@Column(name = "theory_periods", nullable = false)
	private Integer theoryPeriods;

	@Column(name = "practical_periods", nullable = false)
	private Integer practicalPeriods;

	// Quan hệ với Department (nếu cần)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id", insertable = false, updatable = false)
	private Department department;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Integer getTheoryPeriods() {
		return theoryPeriods;
	}

	public void setTheoryPeriods(Integer theoryPeriods) {
		this.theoryPeriods = theoryPeriods;
	}

	public Integer getPracticalPeriods() {
		return practicalPeriods;
	}

	public void setPracticalPeriods(Integer practicalPeriods) {
		this.practicalPeriods = practicalPeriods;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}