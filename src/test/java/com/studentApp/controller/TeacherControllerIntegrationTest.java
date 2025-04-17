package com.studentApp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentApp.dto.request.TeacherCreationRequest;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TeacherControllerIntegrationTest {

	private static final Logger log = LoggerFactory.getLogger(TeacherControllerIntegrationTest.class);

	@Container
	private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("test").withUsername("studentApp").withPassword("1234$");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		log.info("Configuring Testcontainers properties - JDBC URL: {}", postgres.getJdbcUrl());
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
		registry.add("spring.flyway.enabled", () -> "true");
		registry.add("spring.profiles.active", () -> "test");
	}

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;
	private TeacherCreationRequest creationRequest;
	private String adminToken;

	@BeforeEach
	void setUp() throws Exception {
		log.info("Setting up test environment - Database URL: {}", postgres.getJdbcUrl());
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

		creationRequest = TeacherCreationRequest.builder().teacherName("Nguyen Van B")
				.userEmail("nguyenvanb@university.edu.vn").teacherDateOfBirth(Date.valueOf("1981-01-01"))
				.teacherAddress("456 Hanoi").teacherPhoneNumber("0987654321").build();

		log.info("Attempting to login with username: admin1");
		String loginJson = objectMapper.writeValueAsString(new HashMap<String, String>() {
			{
				put("username", "admin1");
				put("password", "admin");
			}
		});
		log.debug("Login request body: {}", loginJson);
		String response = mockMvc
				.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginJson))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		log.info("Login response: {}", response);
		adminToken = response.replaceAll("^\"|\"$", "");
		log.info("Admin token: {}", adminToken);
	}

	@Test
	void createTeacher_Integration_Success() throws Exception {
		log.info("Running createTeacher_Integration_Success test");
		mockMvc.perform(post("/api/users/create/teacher").header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(creationRequest)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.teacherName").value("Nguyen Van B"));
	}
}