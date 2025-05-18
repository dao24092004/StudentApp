package com.studentApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeResponse {

	private Long id;
	private Long studentId;
	private Long classId;
	private String subjectName; // Tên môn học (lấy từ Class)
	private String semesterName; // Tên học kỳ (lấy từ Class hoặc liên kết với Semester)
	private Double attendanceScore;
	private Double examScore;
	private Double finalScore;
	private String note;
}
