package com.studentApp.dto.response;

import java.sql.Date;

import com.studentApp.entity.Teacher.Gender;

import lombok.Data;

@Data
public class TeacherResponse {
    private long id;
    private Long userId;
    private String userEmail;
    private String teacherCode;
    private String teacherName;
    private Date teacherDateOfBirth;
    private Gender teacherGender;
    private String teacherAddress;
    private String teacherPhoneNumber;
    private String teacherEmail;
}