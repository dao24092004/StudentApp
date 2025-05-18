package com.studentApp.dto.request;

import lombok.Data;

@Data
public class SubjectRequestDTO {
    private String subjectCode;
    private String subjectName;
    private Integer credits;
    private String deptName; // Thay vì deptId
    private String description;
    private Integer theoryPeriods;
    private Integer practicalPeriods;
}