package com.studentApp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeCreationRequest {

    private Long class_id;
    private Long student_id;
    private Double attendance_score;
    private Double exam_score;
    private Double final_score;
    private String note;
}
