package com.studentApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.studentApp.dto.request.TeacherCreationRequest;
import com.studentApp.dto.request.TeacherUpdateRequest;
import com.studentApp.dto.response.TeacherResponse;
import com.studentApp.dto.response.UserResponse;
import com.studentApp.entity.Major;
import com.studentApp.entity.Permission;
import com.studentApp.entity.Role;
import com.studentApp.entity.Teacher;
import com.studentApp.entity.User;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.mapper.TeacherMapper;
import com.studentApp.mapper.UserMapper;
import com.studentApp.repository.RoleRepository;
import com.studentApp.repository.TeacherRepository;
import com.studentApp.repository.UserRepository;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;
    public List<TeacherResponse> getAllTeacher(){
        List<Teacher> teacher = teacherRepository.findAll();
        List<TeacherResponse> responses = new ArrayList<>();
        for( Teacher teachers : teacher){
            responses.add(TeacherMapper.toTeacherResponse(teachers));
        }
        return responses;
    }

    public TeacherResponse getByIdTeacher( long id ){
        Teacher teacher = teacherRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return TeacherMapper.toTeacherResponse(teacher);
    }

    public TeacherResponse createTeacher(TeacherCreationRequest request) {
        if (teacherRepository.findByTeacherPhoneNumber(request.getTeacherPhoneNumber()).isPresent()) {
            throw new AppException(ErrorCode.TEACHER_ALLREADY_EXISTS);
        }
    
        User user = userRepository.findByEmail(request.getUserEmail());
        if( user != null ){
            Role roleEntity = roleRepository.findByRoleName(com.studentApp.enums.Role.TEACHER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            user.setRole(roleEntity);

            Teacher teacher = new Teacher();
            String teacherCode = generateTeacherCode(user.getId());
            String teacherEmail = generateEmail(user.getUsername(), teacherCode);
            teacher.setTeacherCode(teacherCode);
            teacher.setTeacherName(request.getTeacherName());
            teacher.setTeacherDateOfBirth(request.getTeacherDateOfBirth());
            teacher.setTeacherGender(request.getTeacherGender());
            teacher.setTeacherAddress(request.getTeacherAddress());
            teacher.setTeacherPhoneNumber(request.getTeacherPhoneNumber());
            teacher.setTeacherEmail(teacherEmail);
            teacher.setUser(user);
            Teacher savedTeacher = teacherRepository.save(teacher);
            return TeacherMapper.toTeacherResponse(savedTeacher);
        }
        else{
            User newUser = new User();
            newUser.setUsername(request.getTeacherName());
            newUser.setPassword(jwtService.encodePassword("password123"));
            
            Role roleEntity = roleRepository.findByRoleName(com.studentApp.enums.Role.TEACHER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                newUser.setRole(roleEntity);
            newUser.setEmail(request.getUserEmail());
            
            User saveUser = userRepository.save(newUser);

            Teacher teacher = new Teacher();
            teacher.setTeacherCode("GV00");
            teacher.setTeacherName(request.getTeacherName());
            teacher.setTeacherDateOfBirth(request.getTeacherDateOfBirth());
            teacher.setTeacherGender(request.getTeacherGender());
            teacher.setTeacherAddress(request.getTeacherAddress());
            teacher.setTeacherPhoneNumber(request.getTeacherPhoneNumber());
            teacher.setTeacherEmail("teacher@university.edu.vn");
            teacher.setUser(saveUser);

            // Lưu lần đầu để có ID tự tăng
            Teacher savedTeacher = teacherRepository.save(teacher);

            // Tạo mã giảng viên và email
            String teacherCode = generateTeacherCode(savedTeacher.getId());
            String teacherEmail = generateEmail(savedTeacher.getTeacherName(), teacherCode);

            savedTeacher.setTeacherCode(teacherCode);
            savedTeacher.setTeacherEmail(teacherEmail);

            // Lưu lại với thông tin đầy đủ
            savedTeacher = teacherRepository.save(savedTeacher);

            return TeacherMapper.toTeacherResponse(savedTeacher);

        }
    }    

    public TeacherResponse updateTeacher( Long id, TeacherUpdateRequest request){

        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
            
            teacher.setTeacherAddress(request.getTeacherAddress());
            teacher.setTeacherDateOfBirth(request.getTeacherDateOfBirth());
            teacher.setTeacherEmail(request.getTeacherEmail());
            teacher.setTeacherGender(request.getTeacherGender());
            teacher.setTeacherName(request.getTeacherName());
            teacher.setTeacherPhoneNumber(request.getTeacherPhoneNumber());

            Teacher updateTeacher = teacherRepository.save(teacher);
            return TeacherMapper.toTeacherResponse(updateTeacher);
    }

    public void deleteTeacher( Long id ){
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow( () ->new AppException(ErrorCode.TEACHER_NOT_FOUND));
        teacherRepository.delete(teacher);
    }

    private String generateTeacherCode(Long id) {
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