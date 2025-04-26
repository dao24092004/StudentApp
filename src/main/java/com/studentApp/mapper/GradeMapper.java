package com.studentApp.mapper;

import com.studentApp.dto.response.GradeResponse;
import com.studentApp.entity.Grade;

public class GradeMapper {

    public static GradeResponse toGradeResponse(Grade grade) {
        GradeResponse response = new GradeResponse();
        response.setClassId(grade.getClazz() != null ? grade.getClazz().getId() : null);
        response.setStudentId(grade.getStudent() != null ? grade.getStudent().getId() : null);
        response.setAttendanceScore(grade.getAttendanceScore());
        response.setExamScore(grade.getExamScore());
        response.setFinalScore(grade.getFinalScore());
        response.setNote(grade.getNote());
        return response;
    }
}
