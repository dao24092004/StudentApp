package com.studentApp.dto.request;

import java.sql.Date;

import com.studentApp.entity.Teacher.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherUpdateRequest {
    private String teacherName;
    private Date teacherDateOfBirth;
    private Gender teacherGender;
    private String teacherAddress;
    private String teacherPhoneNumber;
    private String teacherEmail;
}
