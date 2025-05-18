package com.studentApp.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.studentApp.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		ApiResponse<Map<String, String>> response = new ApiResponse<>("error", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(AppException.class)
	public ResponseEntity<ApiResponse<Map<String, Object>>> handleAppException(AppException ex) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put("errorCode", ex.getErrorCode().getCode());
		errorDetails.put("message", ex.getErrorCode().getMessage());
		ApiResponse<Map<String, Object>> response = new ApiResponse<>("error", errorDetails);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleGeneralException(Exception ex) {
		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put("errorCode", "500");
		errorDetails.put("message", "An unexpected error occurred: " + ex.getMessage());
		ApiResponse<Map<String, String>> response = new ApiResponse<>("error", errorDetails);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}