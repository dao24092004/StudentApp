package com.studentApp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeResponse {

    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("attendance_score")
    private Double attendanceScore;

    @JsonProperty("exam_score")
    private Double examScore;

    @JsonProperty("final_score")
    private Double finalScore;

    @JsonProperty("note")
    private String note;
}
