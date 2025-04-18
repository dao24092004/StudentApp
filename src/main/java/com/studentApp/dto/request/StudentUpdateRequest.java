package com.studentApp.dto.request;

import java.util.Date;

import com.studentApp.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class StudentUpdateRequest {

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @JsonProperty("gender")
    private Gender gender;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;
}