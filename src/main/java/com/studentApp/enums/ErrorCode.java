package com.studentApp.enums;

public enum ErrorCode {
	USER_NOT_FOUND(1001, "User not found"), INVALID_CREDENTIALS(1002, "Invalid username or password"),
	UNAUTHORIZED(1003, "Unauthorized access"), TOKEN_EXPIRED(1004, "Token has expired"),
	INVALID_TOKEN(1005, "Invalid token"), ROLE_NOT_FOUND(1006, "Role not found"),
	USERNAME_ALREADY_EXISTS(1007, "Username already exists"), EMAIL_ALREADY_EXISTS(1008, "Email already exists"),
	INVALID_OAUTH2_USER(1009, "Unable to retrieve user information from OAuth2 provider"),
	DEPARTMENT_NOT_FOUND(1010, "Department not found"), DEPARTMENT_ALREADY_EXISTS(1011, "Department already exists"),
	PERMISSION_ALREADY_EXISTS(1021, "Permeission already exists"), PERMISSION_NOT_FOUND(1022, "Permeission NOT_FOUND"),
	PERMISSION_ALREADY_ASSIGNED(1023, "Permeission already exists"),
	PERMISSION_NOT_ASSIGNED(1024, "Permeission NOT ASSIGNED"),
	PERMISSION_EXISTS_ASSIGNED(1024, "Permeission EXISTS ASSIGNED"),
	MAJOR_CODE_ALREADY_EXISTS(1026, "Major already exists"), INVALID_REFRESH_TOKEN(4444, "Invalid refresh token"),
	REFRESH_TOKEN_EXPIRED(4445, "Refresh token has expired"), INVALID_REQUEST(4446, "Invalid request"),;

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