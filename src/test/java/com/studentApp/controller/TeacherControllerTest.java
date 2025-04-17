package com.studentApp.controller;

import java.sql.Date;

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
import com.studentApp.dto.request.TeacherCreationRequest;
import com.studentApp.dto.response.TeacherResponse;
import com.studentApp.entity.Teacher;
import com.studentApp.security.JwtAuthenticationFilter;
import com.studentApp.service.TeacherService;

@WebMvcTest(value = TeacherController.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class) }, excludeAutoConfiguration = {
				org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
				org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
				org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class })
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class TeacherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TeacherService teacherService;

	private TeacherCreationRequest teacherCreationRequest;
	private TeacherResponse teacherResponse;
	private ObjectMapper objectMapper;

	@BeforeEach
	public void initData() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		teacherCreationRequest = TeacherCreationRequest.builder().teacherName("Nguyen Van A")
				.teacherDateOfBirth(Date.valueOf("1980-01-01")).userEmail("nguyenvana@gmail.com")
				.teacherGender(Teacher.Gender.Female).teacherAddress("123 Hanoi").teacherPhoneNumber("0912345678")
				.build();

		teacherResponse = TeacherResponse.builder().id(1L).userId(1L).teacherName("Nguyen Van A")
				.teacherDateOfBirth(Date.valueOf("1980-01-01")).userEmail("nguyenvana@gmail.com")
				.teacherGender(Teacher.Gender.Female).teacherAddress("123 Hanoi").teacherPhoneNumber("0912345678")
				.teacherCode("GV202500001").teacherEmail("nguyenvana_gv202500001@university.edu.vn").build();
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
	void createTeacher_validRequest_success() throws Exception {
		String content = objectMapper.writeValueAsString(teacherCreationRequest);
		Mockito.when(teacherService.createTeacher(ArgumentMatchers.any(TeacherCreationRequest.class)))
				.thenReturn(teacherResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create/teacher").contentType(MediaType.APPLICATION_JSON)
				.content(content)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
				.andExpect(MockMvcResultMatchers.jsonPath("$.teacherName").value("Nguyen Van A"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userEmail").value("nguyenvana@gmail.com"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
	void createTeacher_emailExists_fail() throws Exception {
		teacherCreationRequest.setUserEmail("existing@example.com");
		String content = objectMapper.writeValueAsString(teacherCreationRequest);
		Mockito.when(teacherService.createTeacher(ArgumentMatchers.any(TeacherCreationRequest.class)))
				.thenThrow(new RuntimeException("Teacher already exists"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create/teacher").contentType(MediaType.APPLICATION_JSON)
				.content(content)).andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.content().string("Error creating teacher: Teacher already exists"));
	}

//	@Test
//	@WithMockUser(username = "admin", authorities = { "USER_CREATE" })
//	void createTeacher_invalidPhoneNumber_fail() throws Exception {
//		teacherCreationRequest.setTeacherPhoneNumber("123"); // Invalid phone number
//		String content = objectMapper.writeValueAsString(teacherCreationRequest);
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create/teacher").contentType(MediaType.APPLICATION_JSON)
//				.content(content)).andExpect(MockMvcResultMatchers.status().isBadRequest());
//	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_VIEW" })
	void getByIdTeacher_validId_success() throws Exception {
		long teacherId = 1L;
		System.out.println("teacherResponse before test: " + teacherResponse.getTeacherName());
		Mockito.when(teacherService.getByIdTeacher(teacherId)).thenReturn(teacherResponse);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/users/teacher/{id}", teacherId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
				.andExpect(MockMvcResultMatchers.jsonPath("$.teacherName").value("Nguyen Van A"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "USER_DELETE" })
	void deleteTeacher_validId_success() throws Exception {
		long teacherId = 1L;
		Mockito.doNothing().when(teacherService).deleteTeacher(teacherId);
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/teacher/{id}", teacherId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Teacher delete successfully"));
	}
}