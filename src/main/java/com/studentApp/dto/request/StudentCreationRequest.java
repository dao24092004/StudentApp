package com.studentApp.dto.request;

import java.util.Date;

import com.studentApp.enums.Gender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor; // Đảm bảo có constructor không tham số

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentCreationRequest {

	private String student_code;
	private String student_name;
	private Date date_of_birth;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private String address;
	private String phone_number;
	private Long major_id;
	private String emailUser;
	private Long class_group_id;
}
