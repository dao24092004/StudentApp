package com.studentApp.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SemesterResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
}
