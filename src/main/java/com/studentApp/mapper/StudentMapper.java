package com.studentApp.mapper;

import com.studentApp.dto.response.StudentResponse;
import com.studentApp.entity.Student;
import com.studentApp.enums.Gender;

public class StudentMapper {
    public static StudentResponse toStudentResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setAddress(student.getAddress());
        response.setDateOfBirth(student.getDateOfBirth());
        response.setGender(
            student.getGender() != null
                ? (student.getGender() == Gender.Male ? "Male" : "Female")
                : "Unknown"
        );
        response.setPhoneNumber(student.getPhoneNumber());
        response.setStudentCode(student.getStudentCode());
        response.setStudentName(student.getStudentName());
        return response;
    }
}