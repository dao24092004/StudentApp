package com.studentApp.dto.request;

import java.util.Date;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // Đảm bảo có constructor không tham số
import com.studentApp.enums.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreationRequest {
    private Integer id;
    private int user_id;
    private String student_code;
    private String student_name;
    private Date date_of_birth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String address;
    private String phone_number;
    private Long major_id; 
    private String emailUser;
}
