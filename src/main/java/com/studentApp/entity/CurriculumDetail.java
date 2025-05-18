package com.studentApp.entity;

import java.time.LocalDateTime;

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
@Table(name = "tbl_curriculum_detail", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "curriculum_id", "subject_id", "semester_id" }) })
public class CurriculumDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curriculum_id", nullable = false)
	private Curriculum curriculum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", nullable = false)
	private Subject subject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_id", nullable = false)
	private Semester semester;

	@Column(name = "is_mandatory", nullable = false)
	private Boolean isMandatory;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	// Thêm các phương thức để lấy ID từ các đối tượng liên quan
	public Long getCurriculumId() {
		return curriculum != null ? curriculum.getId() : null;
	}

	public Long getSubjectId() {
		return subject != null ? subject.getId() : null;
	}

	public Long getSemesterId() {
		return semester != null ? semester.getId() : null;
	}
}