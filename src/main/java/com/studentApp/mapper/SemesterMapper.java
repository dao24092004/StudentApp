package com.studentApp.mapper;

import com.studentApp.dto.response.SemesterResponse;
import com.studentApp.entity.Semester;

public class SemesterMapper {
    public static SemesterResponse toSemesterResponse(Semester semester){
        SemesterResponse response = new SemesterResponse();
        response.setId(semester.getId());
        response.setStartDate(semester.getStartDate());
        response.setEndDate(semester.getEndDate());

        return response;
    }
}
