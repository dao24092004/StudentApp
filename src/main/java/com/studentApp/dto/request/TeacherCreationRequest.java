package com.studentApp.dto.request;

import java.sql.Date;

import com.studentApp.entity.Teacher.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class TeacherCreationRequest {
    private String userEmail;
    private String teacherName;
    private Date teacherDateOfBirth;
    private Gender teacherGender;
    private String teacherAddress;
    private String teacherPhoneNumber;
}