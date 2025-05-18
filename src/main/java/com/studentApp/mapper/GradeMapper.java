package com.studentApp.mapper;

import com.studentApp.dto.response.GradeResponse;
import com.studentApp.entity.Grade;

public class GradeMapper {

	public static GradeResponse toGradeResponse(Grade grade) {
		GradeResponse response = new GradeResponse();
		response.setId(grade.getId());
		response.setStudentId(grade.getStudent().getId());
		response.setClassId(grade.getClazz().getId());
		response.setSubjectName(grade.getClazz().getSubject().getSubjectName()); // Giả định Class có liên kết với
																					// Subject
		response.setSemesterName(grade.getClazz().getSemester().getSemesterName()); // Giả định Class có liên kết với
																					// Semester
		response.setAttendanceScore(grade.getAttendanceScore());
		response.setExamScore(grade.getExamScore());
		response.setFinalScore(grade.getFinalScore());
		response.setNote(grade.getNote());
		return response;
	}
}