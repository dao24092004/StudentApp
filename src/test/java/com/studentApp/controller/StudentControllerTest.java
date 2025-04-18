package com.studentApp.controller;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studentApp.config.TestSecurityConfig;
import com.studentApp.dto.request.StudentCreationRequest;
import com.studentApp.dto.response.StudentResponse;
import com.studentApp.enums.ErrorCode;
import com.studentApp.enums.Gender;
import com.studentApp.exception.AppException;
import com.studentApp.security.JwtAuthenticationFilter;
import com.studentApp.service.StudentService;

@WebMvcTest(value = StudentController.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class) }, excludeAutoConfiguration = {
				org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
				org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
				org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class })
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StudentService studentService;

	private StudentCreationRequest studentCreationRequest;
	private StudentResponse studentResponse;
	private ObjectMapper objectMapper;

	@BeforeEach
	public void initData() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		studentCreationRequest = StudentCreationRequest.builder().student_name("Nguyen Van B")
				.date_of_birth(Date.valueOf("2000-01-01")).emailUser("nguyenvanb@gmail.com").gender(Gender.Female)
				.address("456 Hanoi").phone_number("0987654321").build();

		studentResponse = StudentResponse.builder().studentCode("STU002").studentName("Nguyen Van B")
				.dateOfBirth(Date.valueOf("2000-01-01")).userEmail("nguyenvanb@gmail.com").gender("Female")
				.address("456 Hanoi").phoneNumber("0987654321").studentEmail("svb@university.edu").build();
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
	void createStudent_validRequest_success() throws Exception {
		String content = objectMapper.writeValueAsString(studentCreationRequest);
		Mockito.when(studentService.createStudent(ArgumentMatchers.any(StudentCreationRequest.class)))
				.thenReturn(studentResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/student").contentType(MediaType.APPLICATION_JSON)
				.content(content)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_code").value("STU002"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_name").value("Nguyen Van B"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userEmail").value("nguyenvanb@gmail.com"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
	void createStudent_emailExists_fail() throws Exception {
		studentCreationRequest.setEmailUser("existing@example.com");
		String content = objectMapper.writeValueAsString(studentCreationRequest);
		Mockito.when(studentService.createStudent(ArgumentMatchers.any(StudentCreationRequest.class)))
				.thenThrow(new AppException(ErrorCode.STUDENT_ALREADY_EXISTS));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/student").contentType(MediaType.APPLICATION_JSON)
				.content(content)).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1026))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("STUDENT EXISTS ASSIGNED"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_VIEW" })
	void getByIdStudent_validId_success() throws Exception {
		long studentId = 1L;
		Mockito.when(studentService.getIDStudent(studentId)).thenReturn(studentResponse);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/users/student/{id}", studentId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_code").value("STU002"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_name").value("Nguyen Van B"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_VIEW" })
	void getAllStudents_validRequest_success() throws Exception {
		Mockito.when(studentService.getAllStudent()).thenReturn(List.of(studentResponse));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/users/student").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].student_code").value("STU002"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].student_name").value("Nguyen Van B"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
	void testCreateStudent() throws Exception {
		Mockito.when(studentService.createStudent(ArgumentMatchers.any(StudentCreationRequest.class)))
				.thenReturn(studentResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/student").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(studentCreationRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_code").value("STU002"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_name").value("Nguyen Van B"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userEmail").value("nguyenvanb@gmail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.address").value("456 Hanoi"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value("0987654321"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.studentEmail").value("svb@university.edu"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_UPDATE" })
	void deleteStudent_validId_success() throws Exception {
		long studentId = 1L;
		Mockito.doNothing().when(studentService).deleteStudent(studentId);
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/student/delete/{id}", studentId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Student deleted successfully"));
	}
}