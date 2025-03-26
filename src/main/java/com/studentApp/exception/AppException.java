package com.studentApp.exception;

import com.studentApp.enums.ErrorCode;

public class AppException extends RuntimeException {

	private final ErrorCode errorCode;

	public AppException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public AppException(ErrorCode userNotFound, String string) {
		// TODO Auto-generated constructor stub
		super(string);
		this.errorCode = userNotFound;

	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}