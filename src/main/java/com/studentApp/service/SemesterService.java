package com.studentApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.studentApp.entity.Semester;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.repository.SemesterRepository;
import com.studentApp.repository.ClassGroupRepository;
import jakarta.transaction.Transactional;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Transactional
    public Semester addSemester(Semester semester) {
        return semesterRepository.save(semester);
    }

    @Transactional
    public void deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND)); // Tạo error code riêng nếu cần
        semesterRepository.delete(semester);
    }
}