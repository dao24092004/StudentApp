package com.studentApp.dto.response;

import java.util.Map;

public class ApiResponse<T> {
	private String status;
	private String message;
	private T data;
	private Map<String, String> errors;

	// Constructor cho phản hồi thành công
	public ApiResponse(String status, T data) {
		this.status = status;
		this.data = data;
	}

	// Constructor cho phản hồi lỗi (với thông điệp)
	public ApiResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	// Constructor cho phản hồi lỗi (với danh sách lỗi)
	public ApiResponse(String status, Map<String, String> errors) {
		this.status = status;
		this.errors = errors;
	}

	// Getters và setters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}