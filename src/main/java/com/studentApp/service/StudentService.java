package com.studentApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.repository.ClassGroupRepository;
import com.studentApp.repository.MajorRepository;
import com.studentApp.repository.RoleRepository;
import com.studentApp.repository.StudentRepository;
import com.studentApp.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.studentApp.dto.request.StudentCreationRequest;
import com.studentApp.dto.request.StudentUpdateRequest;
import com.studentApp.dto.response.StudentResponse;
import com.studentApp.entity.ClassGroup;
import com.studentApp.entity.Major;
import com.studentApp.entity.Student;
import com.studentApp.entity.User;
import com.studentApp.enums.ErrorCode;
import com.studentApp.enums.Role;
import com.studentApp.exception.AppException;
import com.studentApp.mapper.StudentMapper;

@Service
public class StudentService {

    private final JwtService jwtService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private MajorRepository majorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClassGroupRepository classGroupRepository;


    StudentService(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    public List<StudentResponse> getAllStudent() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponse> response = new ArrayList<>();
        for (Student student : students) {
            response.add(StudentMapper.toStudentResponse(student));
        }
        return response;
    }

    public StudentResponse getIDStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return StudentMapper.toStudentResponse(student);
    }

    public StudentResponse createStudent(StudentCreationRequest request) {
        if (studentRepository.existsByStudentCode(request.getStudent_code())) {
            throw new AppException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }
    
        User user = userRepository.findByEmail(request.getEmailUser());
    
        // Kiểm tra nếu email đã tồn tại trong hệ thống
        if (user != null) {
            // Nếu user đã tồn tại, gán role cho user và lưu lại
            Role studentRoleEnum = Role.STUDENT;
            com.studentApp.entity.Role studentRoleEntity = roleRepository.findByRoleName(studentRoleEnum.name())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    
            user.setRole(studentRoleEntity);
            userRepository.save(user); // Lưu user đã được cập nhật
    
            // Thêm thông tin sinh viên
            Major major = majorRepository.findById(request.getMajor_id())
                    .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
    
            ClassGroup classGroup = classGroupRepository.findById(request.getClass_group_id())
                    .orElseThrow(() -> new AppException(ErrorCode.CLASS_GROUP_NOT_FOUND));
    
            Student studentAdd = new Student();
            studentAdd.setAddress(request.getAddress());
            studentAdd.setDateOfBirth(request.getDate_of_birth());
            studentAdd.setGender(request.getGender());
            studentAdd.setPhoneNumber(request.getPhone_number());
            studentAdd.setStudentCode(request.getStudent_code());
            studentAdd.setStudentName(request.getStudent_name());
            studentAdd.setMajor(major);
            studentAdd.setClassGroup(classGroup);
    
            studentAdd = studentRepository.save(studentAdd);
    
            // Tạo StudentResponse
            StudentResponse response = StudentMapper.toStudentResponse(studentAdd);
            response.setUserEmail(user.getEmail());  // Set email của User vào StudentResponse
            response.setStudentEmail(user.getEmail());  // Set email của User vào StudentResponse nếu cần
    
            return response;
        } else {
            // Nếu user chưa tồn tại, tạo mới user với email từ request
            User newUser = new User();
            newUser.setUsername(request.getStudent_name());
            newUser.setPassword(jwtService.encodePassword("password123"));
    
            Role studentRoleEnum = Role.STUDENT;
            com.studentApp.entity.Role studentRoleEntity = roleRepository.findByRoleName(studentRoleEnum.name())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    
            newUser.setRole(studentRoleEntity);
            newUser.setEmail(request.getEmailUser()); // Gán email nhập từ bên ngoài vào
    
            userRepository.save(newUser); // Lưu user mới
    
            Major major = majorRepository.findById(request.getMajor_id())
                    .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
    
            ClassGroup classGroup = classGroupRepository.findById(request.getClass_group_id())
                    .orElseThrow(() -> new AppException(ErrorCode.CLASS_GROUP_NOT_FOUND));
    
            Student studentAdd = new Student();
            studentAdd.setAddress(request.getAddress());
            studentAdd.setDateOfBirth(request.getDate_of_birth());
            studentAdd.setGender(request.getGender());
            studentAdd.setPhoneNumber(request.getPhone_number());
            studentAdd.setStudentCode(request.getStudent_code());
            studentAdd.setStudentName(request.getStudent_name());
            studentAdd.setMajor(major);
            studentAdd.setClassGroup(classGroup);
    
            studentAdd = studentRepository.save(studentAdd);
    
            // Tạo StudentResponse
            StudentResponse response = StudentMapper.toStudentResponse(studentAdd);
            response.setUserEmail(newUser.getEmail());  // Set email của User vào StudentResponse
            response.setStudentEmail(newUser.getEmail());  // Set email của User vào StudentResponse nếu cần
    
            return response;
        }
    
    
    }
    
    
    
    

    public StudentResponse updateStudent(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        student.setAddress(request.getAddress());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setStudentName(request.getStudentName());


        Student updatedStudent = studentRepository.save(student);
        return StudentMapper.toStudentResponse(updatedStudent);

        
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

    User user = student.getUser();
    
    studentRepository.delete(student); // Xóa student trước
    if (user != null) {
        userRepository.delete(user);   // Xóa user sau
    }
    }
    

    private String generateStudentCode(Long id) {
        int currentYear = java.time.Year.now().getValue();
        return "GV" + currentYear + String.format("%05d", id); // Thêm padding để mã đẹp như GV202500001
    }
    
    private String generateEmail(String name, String teacherCode) {
        String normalized = name.trim().toLowerCase()
            .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
            .replaceAll("[èéẹẻẽêềếệểễ]", "e")
            .replaceAll("[ìíịỉĩ]", "i")
            .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
            .replaceAll("[ùúụủũưừứựửữ]", "u")
            .replaceAll("[ỳýỵỷỹ]", "y")
            .replaceAll("đ", "d")
            .replaceAll("[^a-z0-9]", ""); // loại bỏ ký tự đặc biệt và khoảng trắng
        return normalized + "_" + teacherCode.toLowerCase() + "@university.edu.vn";
    }

}