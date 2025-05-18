package com.studentApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.GradeCreationRequest;
import com.studentApp.dto.request.GradeUpdateRequest;
import com.studentApp.dto.response.GradeResponse;
import com.studentApp.entity.Class;
import com.studentApp.entity.Grade;
import com.studentApp.entity.Student;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.mapper.GradeMapper;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.GradeRepository;
import com.studentApp.repository.StudentRepository;

@Service
public class GradeService {

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ClassRepository classRepository;

	public List<GradeResponse> getAllGrades() {
		List<Grade> grades = gradeRepository.findAll();
		List<GradeResponse> responses = new ArrayList<>();
		for (Grade grade : grades) {
			responses.add(GradeMapper.toGradeResponse(grade));
		}
		return responses;
	}

	public GradeResponse getGradeById(Long id) {
		Grade grade = gradeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.GRADE_NOT_FOUND));
		return GradeMapper.toGradeResponse(grade);
	}

	public GradeResponse createGrade(GradeCreationRequest request) {
		// Kiểm tra student tồn tại
		Student student = studentRepository.findById(request.getStudent_id())
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		// Kiểm tra class tồn tại
		Class classEntity = classRepository.findById(request.getClass_id())
				.orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

		// Tạo mới Grade
		Grade grade = new Grade();
		grade.setStudent(student);
		grade.setClazz(classEntity);
		grade.setAttendanceScore(request.getAttendance_score());
		grade.setExamScore(request.getExam_score());
		grade.setFinalScore(request.getFinal_score());
		grade.setNote(request.getNote());

		grade = gradeRepository.save(grade);
		return GradeMapper.toGradeResponse(grade);
	}

	public GradeResponse updateGrade(Long id, GradeUpdateRequest request) {
		Grade grade = gradeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.GRADE_NOT_FOUND));

		grade.setAttendanceScore(request.getAttendance_score());
		grade.setExamScore(request.getExam_score());
		grade.setFinalScore(request.getFinal_score());
		grade.setNote(request.getNote());
		Grade updatedGrade = gradeRepository.save(grade);
		return GradeMapper.toGradeResponse(updatedGrade);
	}

	public void deleteGrade(Long id) {
		Grade grade = gradeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.GRADE_NOT_FOUND));
		gradeRepository.delete(grade);
	}

	public List<GradeResponse> getGradesByStudentId(Long studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		List<Grade> grades = gradeRepository.findByStudent(student);
		List<GradeResponse> responses = new ArrayList<>();
		for (Grade grade : grades) {
			responses.add(GradeMapper.toGradeResponse(grade));
		}
		return responses;
	}

	public List<GradeResponse> getGradesForCurrentStudent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new AppException(ErrorCode.GRADE_NOT_FOUND);
		}

		String username = authentication.getName();
		Student student = studentRepository.findByUserUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		List<Grade> grades = gradeRepository.findByStudent(student);
		List<GradeResponse> responses = new ArrayList<>();
		for (Grade grade : grades) {
			responses.add(GradeMapper.toGradeResponse(grade));
		}
		return responses;
	}

	// Thêm phương thức để cập nhật điểm theo studentId
	@Transactional
	public List<GradeResponse> updateGradesByStudentId(Long studentId, List<GradeUpdateRequest> requests) {
		// Kiểm tra sinh viên tồn tại
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		// Lấy danh sách điểm hiện tại của sinh viên
		List<Grade> existingGrades = gradeRepository.findByStudent(student);
		if (existingGrades.isEmpty()) {
			throw new AppException(ErrorCode.GRADE_NOT_FOUND, "No grades found for student with ID: " + studentId);
		}

		List<GradeResponse> updatedGrades = new ArrayList<>();

		// Duyệt qua danh sách request để cập nhật điểm
		for (GradeUpdateRequest request : requests) {
			// Tìm Grade tương ứng với classId trong request
			Long classId = request.getClass_id();
			if (classId == null) {
				throw new AppException(ErrorCode.INVALID_CREDENTIALS,
						"Class ID must not be null in grade update request");
			}

			Grade gradeToUpdate = existingGrades.stream().filter(grade -> grade.getClazz().getId().equals(classId))
					.findFirst().orElseThrow(() -> new AppException(ErrorCode.GRADE_NOT_FOUND,
							"Grade not found for student ID " + studentId + " and class ID " + classId));

			// Cập nhật các trường điểm
			if (request.getAttendance_score() != null) {
				gradeToUpdate.setAttendanceScore(request.getAttendance_score());
			}
			if (request.getExam_score() != null) {
				gradeToUpdate.setExamScore(request.getExam_score());
			}
			if (request.getFinal_score() != null) {
				gradeToUpdate.setFinalScore(request.getFinal_score());
			}
			if (request.getNote() != null) {
				gradeToUpdate.setNote(request.getNote());
			}

			// Lưu Grade đã cập nhật
			gradeRepository.save(gradeToUpdate);
			updatedGrades.add(GradeMapper.toGradeResponse(gradeToUpdate));
		}

		return updatedGrades;
	}
}