package com.studentApp.dto.response;

import lombok.Data;

@Data
public class SubjectResponseDTO {
    private Long id;
    private String subjectCode;
    private String subjectName;
    private Integer credits;
    private String deptName;
    private String description;
    private Integer theoryPeriods; // Added
    private Integer practicalPeriods; // Added
}