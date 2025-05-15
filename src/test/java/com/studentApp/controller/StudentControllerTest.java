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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studentApp.config.TestSecurityConfig;
import com.studentApp.dto.request.StudentCreationRequest;
import com.studentApp.dto.response.StudentResponse;
import com.studentApp.enums.ErrorCode;
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

		studentCreationRequest = StudentCreationRequest.builder().student_name("Nguyễn Văn Bde")
				.date_of_birth(Date.valueOf("2001-12-26")).emailUser("fsd33ff3dsd@gmail.com").gender("Male")
				.address("Hà Nội").phone_number("09889227425").major_id(2L).class_group_id(2L).build();

		studentResponse = StudentResponse.builder().studentCode("SV2025000133").studentName("Nguyễn Văn Bde")
				.dateOfBirth(null).userEmail("fsd33ff3dsd@gmail.com").gender("Male").address("Hà Nội")
				.phoneNumber("09889227425").studentEmail(null).majorName("Thuong mại điện tử").build();
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
	void createStudent_validRequest_success() throws Exception {
		String content = objectMapper.writeValueAsString(studentCreationRequest);
		Mockito.when(studentService.createStudent(ArgumentMatchers.any(StudentCreationRequest.class)))
				.thenReturn(studentResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/student").contentType(MediaType.APPLICATION_JSON)
				.content(content)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_code").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_name").value("Nguyễn Văn Bde"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user_email").value("fsd33ff3dsd@gmail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Hà Nội"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value("09889227425"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
	void createStudent_emailExists_fail() throws Exception {
		studentCreationRequest.setEmailUser("existing@example.com");
		String content = objectMapper.writeValueAsString(studentCreationRequest);
		Mockito.when(studentService.createStudent(ArgumentMatchers.any(StudentCreationRequest.class)))
				.thenThrow(new AppException(ErrorCode.USERNAME_ALREADY_EXISTS));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/student").contentType(MediaType.APPLICATION_JSON)
				.content(content)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.errorCode").value(1007))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("Username already exists"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_VIEW" })
	void getByIdStudent_validId_success() throws Exception {
		long studentId = 1L;
		Mockito.when(studentService.getIDStudent(studentId)).thenReturn(studentResponse);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/users/student/{id}", studentId)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_code").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.student_name").value("Nguyễn Văn Bde"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user_email").value("fsd33ff3dsd@gmail.com"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_VIEW" })
	void getAllStudents_validRequest_success() throws Exception {
		Mockito.when(studentService.getAllStudent()).thenReturn(List.of(studentResponse));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/users/student").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].student_code").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].student_name").value("Nguyễn Văn Bde"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].user_email").value("fsd33ff3dsd@gmail.com"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_DELETE" })
	void deleteStudent_validId_success() throws Exception {
		long studentId = 1L;
		Mockito.doNothing().when(studentService).deleteStudent(studentId);
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/student/delete/{id}", studentId)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Student deleted successfully"));
	}
}