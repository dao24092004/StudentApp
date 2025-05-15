package com.studentApp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeUpdateRequest {
	private Long class_id; // Thêm class_id để xác định lớp học
	private Double attendance_score;
	private Double exam_score;
	private Double final_score;
	private String note;
}
