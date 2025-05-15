package com.studentApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.CurriculumDetailRequestDTO;
import com.studentApp.dto.request.CurriculumRequestDTO;
import com.studentApp.dto.response.CurriculumDetailResponseDTO;
import com.studentApp.dto.response.CurriculumResponseDTO;
import com.studentApp.entity.Curriculum;
import com.studentApp.entity.CurriculumDetail;
import com.studentApp.entity.Semester;
import com.studentApp.entity.Student; // Thêm import
import com.studentApp.entity.Subject;
import com.studentApp.repository.CurriculumDetailRepository;
import com.studentApp.repository.CurriculumRepository;
import com.studentApp.repository.SemesterRepository;
import com.studentApp.repository.StudentRepository; // Thêm import
import com.studentApp.repository.SubjectRepository;

@Service
public class CurriculumService {

	private static final Logger logger = LoggerFactory.getLogger(CurriculumService.class);

	@Autowired
	private CurriculumRepository curriculumRepository;

	@Autowired
	private CurriculumDetailRepository curriculumDetailRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private SemesterRepository semesterRepository;

	@Autowired
	private StudentRepository studentRepository; // Thêm dependency

	@Transactional
	public CurriculumResponseDTO createCurriculum(CurriculumRequestDTO dto) {
		Curriculum curriculum = new Curriculum();
		curriculum.setCurriculumCode(dto.getCurriculumCode());
		curriculum.setCurriculumName(dto.getCurriculumName());
		curriculum.setDescription(dto.getDescription());
		curriculum.setCreatedAt(LocalDateTime.now());
		curriculum.setUpdatedAt(LocalDateTime.now());

		Curriculum savedCurriculum = curriculumRepository.save(curriculum);
		return mapToCurriculumResponseDTO(savedCurriculum);
	}

	public List<CurriculumResponseDTO> getAllCurriculums() {
		return curriculumRepository.findAll().stream().map(this::mapToCurriculumResponseDTO).toList();
	}

	@Transactional
	public CurriculumResponseDTO updateCurriculum(Long id, CurriculumRequestDTO dto) {
		Optional<Curriculum> optionalCurriculum = curriculumRepository.findById(id);
		if (optionalCurriculum.isEmpty()) {
			throw new RuntimeException("Curriculum not found with id: " + id);
		}

		Curriculum curriculum = optionalCurriculum.get();
		curriculum.setCurriculumCode(dto.getCurriculumCode());
		curriculum.setCurriculumName(dto.getCurriculumName());
		curriculum.setDescription(dto.getDescription());
		curriculum.setUpdatedAt(LocalDateTime.now());

		Curriculum updatedCurriculum = curriculumRepository.save(curriculum);
		return mapToCurriculumResponseDTO(updatedCurriculum);
	}

	@Transactional
	public void deleteCurriculum(Long id) {
		curriculumRepository.deleteById(id);
	}

	@Transactional
	public CurriculumDetailResponseDTO createCurriculumDetail(CurriculumDetailRequestDTO dto) {
		CurriculumDetail detail = new CurriculumDetail();
		Curriculum curriculum = curriculumRepository.findById(dto.getCurriculumId())
				.orElseThrow(() -> new RuntimeException("Curriculum not found with id: " + dto.getCurriculumId()));
		Subject subject = subjectRepository.findById(dto.getSubjectId())
				.orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getSubjectId()));
		Semester semester = semesterRepository.findById(dto.getSemesterId())
				.orElseThrow(() -> new RuntimeException("Semester not found with id: " + dto.getSemesterId()));

		detail.setCurriculum(curriculum);
		detail.setSubject(subject);
		detail.setSemester(semester);
		detail.setIsMandatory(dto.getIsMandatory() != null ? dto.getIsMandatory() : true);
		detail.setCreatedAt(LocalDateTime.now());

		CurriculumDetail savedDetail = curriculumDetailRepository.save(detail);
		return mapToCurriculumDetailResponseDTO(savedDetail);
	}

	public List<CurriculumDetailResponseDTO> getCurriculumDetails(Long curriculumId) {
		List<CurriculumDetail> details = curriculumDetailRepository.findByCurriculumIdWithDetails(curriculumId);
		logger.debug("Found {} curriculum details for curriculumId: {}", details.size(), curriculumId);
		return details.stream().map(this::mapToCurriculumDetailResponseDTO).toList();
	}

	// Thêm phương thức mới để lấy chương trình khung của sinh viên
	public List<CurriculumDetailResponseDTO> getCurriculumDetailsByStudentId(Long studentId) {
		// Tìm sinh viên theo studentId
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

		// Lấy curriculumId từ major của sinh viên
		Long curriculumId = student.getMajor().getCurriculum() != null ? student.getMajor().getCurriculum().getId()
				: null;

		if (curriculumId == null) {
			throw new RuntimeException("Curriculum not found for student with id: " + studentId);
		}

		// Lấy danh sách CurriculumDetail dựa trên curriculumId
		List<CurriculumDetail> details = curriculumDetailRepository.findByCurriculumIdWithDetails(curriculumId);
		logger.debug("Found {} curriculum details for studentId: {}, curriculumId: {}", details.size(), studentId,
				curriculumId);
		return details.stream().map(this::mapToCurriculumDetailResponseDTO).toList();
	}

	@Transactional
	public CurriculumDetailResponseDTO updateCurriculumDetail(Long id, CurriculumDetailRequestDTO dto) {
		Optional<CurriculumDetail> optionalDetail = curriculumDetailRepository.findById(id);
		if (optionalDetail.isEmpty()) {
			throw new RuntimeException("Curriculum detail not found with id: " + id);
		}

		CurriculumDetail detail = optionalDetail.get();
		Curriculum curriculum = curriculumRepository.findById(dto.getCurriculumId())
				.orElseThrow(() -> new RuntimeException("Curriculum not found with id: " + dto.getCurriculumId()));
		Subject subject = subjectRepository.findById(dto.getSubjectId())
				.orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getSubjectId()));
		Semester semester = semesterRepository.findById(dto.getSemesterId())
				.orElseThrow(() -> new RuntimeException("Semester not found with id: " + dto.getSemesterId()));

		detail.setCurriculum(curriculum);
		detail.setSubject(subject);
		detail.setSemester(semester);
		detail.setIsMandatory(dto.getIsMandatory() != null ? dto.getIsMandatory() : detail.getIsMandatory());
		detail.setCreatedAt(detail.getCreatedAt());

		CurriculumDetail updatedDetail = curriculumDetailRepository.save(detail);
		return mapToCurriculumDetailResponseDTO(updatedDetail);
	}

	@Transactional
	public void deleteCurriculumDetail(Long id) {
		curriculumDetailRepository.deleteById(id);
	}

	private CurriculumResponseDTO mapToCurriculumResponseDTO(Curriculum curriculum) {
		CurriculumResponseDTO dto = new CurriculumResponseDTO();
		dto.setId(curriculum.getId());
		dto.setCurriculumCode(curriculum.getCurriculumCode());
		dto.setCurriculumName(curriculum.getCurriculumName());
		dto.setDescription(curriculum.getDescription());
		dto.setCreatedAt(curriculum.getCreatedAt());
		dto.setUpdatedAt(curriculum.getUpdatedAt());
		return dto;
	}

	private CurriculumDetailResponseDTO mapToCurriculumDetailResponseDTO(CurriculumDetail detail) {
		CurriculumDetailResponseDTO dto = new CurriculumDetailResponseDTO();
		dto.setId(detail.getId());
		dto.setCurriculumId(detail.getCurriculumId());
		dto.setSubjectId(detail.getSubjectId());
		dto.setSemesterId(detail.getSemesterId());
		dto.setIsMandatory(detail.getIsMandatory());
		dto.setCreatedAt(detail.getCreatedAt());

		if (detail.getCurriculum() != null) {
			dto.setCurriculumName(detail.getCurriculum().getCurriculumName());
			logger.debug("Curriculum found: curriculumId={}, curriculumName={}", detail.getCurriculumId(),
					detail.getCurriculum().getCurriculumName());
		} else {
			logger.warn("Curriculum not found for curriculumId: {}", detail.getCurriculumId());
			dto.setCurriculumName("Unknown Curriculum");
		}

		if (detail.getSubject() != null) {
			dto.setSubjectName(detail.getSubject().getSubjectName());
			dto.setCredits(detail.getSubject().getCredits());
			dto.setTheoryPeriods(detail.getSubject().getTheoryPeriods());
			dto.setPracticalPeriods(detail.getSubject().getPracticalPeriods());
			logger.debug("Subject found: subjectId={}, subjectName={}", detail.getSubjectId(),
					detail.getSubject().getSubjectName());
		} else {
			logger.warn("Subject not found for subjectId: {}", detail.getSubjectId());
			dto.setSubjectName("Unknown Subject");
		}

		if (detail.getSemester() != null) {
			dto.setSemesterName(detail.getSemester().getSemesterName());
			logger.debug("Semester found: semesterId={}, semesterName={}", detail.getSemesterId(),
					detail.getSemester().getSemesterName());
		} else {
			logger.warn("Semester not found for semesterId: {}", detail.getSemesterId());
			dto.setSemesterName("Unknown Semester");
		}

		return dto;
	}
}