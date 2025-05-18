package com.studentApp.enums;

public enum ErrorCode {
	USER_NOT_FOUND(1001, "User not found"), INVALID_CREDENTIALS(1002, "Invalid username or password"),
	UNAUTHORIZED(1003, "Unauthorized access"), TOKEN_EXPIRED(1004, "Token has expired"),
	INVALID_TOKEN(1005, "Invalid token"), ROLE_NOT_FOUND(1006, "Role not found"),
	USERNAME_ALREADY_EXISTS(1007, "Username already exists"), EMAIL_ALREADY_EXISTS(1008, "Email already exists"),
	INVALID_OAUTH2_USER(1009, "Unable to retrieve user information from OAuth2 provider"),
	DEPARTMENT_NOT_FOUND(1010, "Department not found"), DEPARTMENT_ALREADY_EXISTS(1011, "Department already exists"),
	PERMISSION_ALREADY_EXISTS(1021, "Permission already exists"), PERMISSION_NOT_FOUND(1022, "Permission not found"),
	PERMISSION_ALREADY_ASSIGNED(1023, "Permission already assigned"),
	PERMISSION_NOT_ASSIGNED(1024, "Permission not assigned"),
	PERMISSION_EXISTS_ASSIGNED(1025, "Permission exists assigned"),
	MAJOR_CODE_ALREADY_EXISTS(1026, "Major already exists"), INVALID_REFRESH_TOKEN(4444, "Invalid refresh token"),
	REFRESH_TOKEN_EXPIRED(4445, "Refresh token has expired"), INVALID_REQUEST(4446, "Invalid request"),
	TEACHER_NOT_FOUND(3001, "Teacher not found"), TEACHER_ALLREADY_EXISTS(3002, "Teacher already exists"),
	STUDENT_NOT_FOUND(1027, "Student not found"), STUDENT_ALREADY_EXISTS(1028, "Student already exists"),
	MAJOR_NOT_FOUND(1029, "Major not found"), GRADE_NOT_FOUND(1030, "Grade not found"),
	CLASS_NOT_FOUND(1031, "Class not found");

	private final int code;
	private final String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}