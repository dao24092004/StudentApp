package com.studentApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.SemesterRequest;
import com.studentApp.entity.Semester;
import com.studentApp.service.SemesterService;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping("/create")
    public ResponseEntity<Object> createSemester(@Valid @RequestBody SemesterRequest semesterRequest) {
        try {
            System.out.println("StartDate: " + semesterRequest.getStartDate());
            System.out.println("EndDate: " + semesterRequest.getEndDate());

            Semester semester = new Semester();
            semester.setStartDate(semesterRequest.getStartDate());
            semester.setEndDate(semesterRequest.getEndDate());

            Semester savedSemester = semesterService.addSemester(semester);
            return new ResponseEntity<>(savedSemester, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse("Cannot create semester: Invalid data provided."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse("An unexpected error occurred while creating semester."));
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteSemester(@PathVariable Long id) {
        try {
            semesterService.deleteSemester(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse("Cannot delete semester with id " + id + " because it is referenced by class groups or other entities."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse("An unexpected error occurred while deleting semester with id " + id));
        }
    }

    // Xử lý lỗi validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse("Validation failed");
        errorResponse.setErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

// Class phụ để trả về thông báo lỗi
class ErrorResponse {
    private String status = "error";
    private String message;
    private Object data = null;
    private Object errors = null;

    public ErrorResponse(String message) {
        this.message = message;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}