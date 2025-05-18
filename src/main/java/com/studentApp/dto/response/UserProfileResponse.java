package com.studentApp.dto.response;

import com.studentApp.entity.Role;

import lombok.Data;

@Data
public class UserProfileResponse {
	private Long id;
	private String username;
	private String email;
	private Role role;
	private String avatarUrl;
	private String createdAt;
	private String updatedAt;
	private boolean credentialsNonExpired;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private String status; // Thêm trường trạng thái
	private String gender; // Thêm giới tính
	private String birthDate; // Thêm ngày sinh
	private Long studentId; // Thêm mã học sinh
	private String studentCode; // Thêm mã học sinh

	private String className; // Thêm lớp
	private String course; // Thêm khóa
	private String ethnicity; // Thêm dân tộc
	private String religion; // Thêm tôn giáo
	private String address; // Thêm nơi học
	private String hometown; // Thêm quê quán
	private String major; // Thêm ngành
	private String department; // Thêm chuyên ngành
	private String classCode; // Thêm mã lớp
	private String phone; // Thêm số điện thoại
	private String job; // Thêm công tác đoàn
}