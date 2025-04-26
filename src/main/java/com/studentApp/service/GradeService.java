package com.studentApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.dto.request.GradeCreationRequest;
import com.studentApp.dto.request.GradeUpdateRequest;
import com.studentApp.dto.response.GradeResponse;
import com.studentApp.entity.Grade;
import com.studentApp.entity.Student;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.mapper.GradeMapper;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.GradeRepository;
import com.studentApp.repository.StudentRepository;
import com.studentApp.entity.Class;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    public List<GradeResponse> getAllGrades() {
        List<Grade> grades = gradeRepository.findAll();
        List<GradeResponse> responses = new ArrayList<>();
        for (Grade grade : grades) {
            responses.add(GradeMapper.toGradeResponse(grade));
        }
        return responses;
    }

    public GradeResponse getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GRADE_NOT_FOUND));
        return GradeMapper.toGradeResponse(grade);
    }
    public GradeResponse createGrade(GradeCreationRequest request) {
    // Kiểm tra student tồn tại
    Student student = studentRepository.findById(request.getStudent_id())
            .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

    // Kiểm tra class tồn tại
    Class classEntity = classRepository.findById(request.getClass_id())
          .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

    // Tạo mới Grade
    Grade grade = new Grade();
    grade.setStudent(student);
    grade.setClassEntity(classEntity);
    grade.setAttendanceScore(request.getAttendance_score());
    grade.setExamScore(request.getExam_score());
    grade.setFinalScore(request.getFinal_score());
    grade.setNote(request.getNote());

    grade = gradeRepository.save(grade);
    return GradeMapper.toGradeResponse(grade);
    }
    
    public GradeResponse updateGrade(Long id, GradeUpdateRequest request) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GRADE_NOT_FOUND));

        grade.setAttendanceScore(request.getAttendance_score());
        grade.setExamScore(request.getExam_score());
        grade.setFinalScore(request.getFinal_score());
        grade.setNote(request.getNote());
        Grade updatedGrade = gradeRepository.save(grade);
        return GradeMapper.toGradeResponse(updatedGrade);
    }
    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GRADE_NOT_FOUND)); // Kiểm tra nếu không tìm thấy grade
    
        gradeRepository.delete(grade); 
    }
}
