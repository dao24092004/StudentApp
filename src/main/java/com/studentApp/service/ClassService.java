package com.studentApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.ClassRequestDTO;
import com.studentApp.dto.response.ClassResponseDTO;
import com.studentApp.entity.Class;
import com.studentApp.entity.ClassGroup;
import com.studentApp.entity.Subject;
import com.studentApp.entity.Teacher;
import com.studentApp.repository.ClassGroupRepository;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.SubjectRepository;
import com.studentApp.repository.TeacherRepository;

@Service
public class ClassService {
	@Autowired
	private ClassRepository classRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private ClassGroupRepository classGroupRepository;

	@Transactional
	public ClassResponseDTO createClass(ClassRequestDTO dto) {
		Class classEntity = new Class();
		classEntity.setClassCode(dto.getClassCode());
		classEntity.setClassName(dto.getClassName());
		Subject subject = subjectRepository.findBySubjectName(dto.getSubjectName())
				.orElseThrow(() -> new RuntimeException("Subject not found: " + dto.getSubjectName()));
		classEntity.setSubject(subject);
		Teacher teacher = teacherRepository.findByTeacherName(dto.getTeacherName())
				.orElseThrow(() -> new RuntimeException("Teacher not found: " + dto.getTeacherName()));
		classEntity.setTeacher(teacher);
		ClassGroup classGroup = classGroupRepository.findByGroupCode(dto.getGroupCode())
				.orElseThrow(() -> new RuntimeException("ClassGroup not found: " + dto.getGroupCode()));
		classEntity.setClassGroup(classGroup);
		classEntity.setStartDate(dto.getStartDate());
		classEntity.setEndDate(dto.getEndDate());
		classEntity.setClassroom(dto.getClassroom());
		classEntity.setShift(dto.getShift());
		classEntity.setPriority(dto.getPriority());
		classEntity = classRepository.save(classEntity);
		return mapToClassResponseDTO(classEntity);
	}

	@Transactional
	public ClassResponseDTO updateClass(Long id, ClassRequestDTO dto) {
		Class classEntity = classRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
		classEntity.setClassCode(dto.getClassCode());
		classEntity.setClassName(dto.getClassName());
		Subject subject = subjectRepository.findBySubjectName(dto.getSubjectName())
				.orElseThrow(() -> new RuntimeException("Subject not found: " + dto.getSubjectName()));
		classEntity.setSubject(subject);
		Teacher teacher = teacherRepository.findByTeacherName(dto.getTeacherName())
				.orElseThrow(() -> new RuntimeException("Teacher not found: " + dto.getTeacherName()));
		classEntity.setTeacher(teacher);
		ClassGroup classGroup = classGroupRepository.findByGroupCode(dto.getGroupCode())
				.orElseThrow(() -> new RuntimeException("ClassGroup not found: " + dto.getGroupCode()));
		classEntity.setClassGroup(classGroup);
		classEntity.setStartDate(dto.getStartDate());
		classEntity.setEndDate(dto.getEndDate());
		classEntity.setClassroom(dto.getClassroom());
		classEntity.setShift(dto.getShift());
		classEntity.setPriority(dto.getPriority());
		classEntity = classRepository.save(classEntity);
		return mapToClassResponseDTO(classEntity);
	}

	@Transactional
	public void deleteClass(Long id) {
		Class classEntity = classRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
		classRepository.delete(classEntity);
	}

	public List<ClassResponseDTO> getAllClasses() {
		return classRepository.findAll().stream().map(this::mapToClassResponseDTO).collect(Collectors.toList());
	}

	private ClassResponseDTO mapToClassResponseDTO(Class classEntity) {
		ClassResponseDTO dto = new ClassResponseDTO();
		dto.setClassCode(classEntity.getClassCode());
		dto.setClassName(classEntity.getClassName());
		dto.setSubjectName(classEntity.getSubject().getSubjectName());
		dto.setTeacherName(classEntity.getTeacher().getTeacherName());
		dto.setGroupCode(classEntity.getClassGroup().getGroupCode());
		dto.setStartDate(classEntity.getStartDate());
		dto.setEndDate(classEntity.getEndDate());
		dto.setClassroom(classEntity.getClassroom());
		dto.setShift(classEntity.getShift());
		dto.setPriority(classEntity.getPriority());
		return dto;
	}
}