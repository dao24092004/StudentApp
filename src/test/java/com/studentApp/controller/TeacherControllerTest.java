package com.studentApp.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TeacherApiE2ETest {

	private static final Logger log = LoggerFactory.getLogger(TeacherApiE2ETest.class);

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

	@LocalServerPort
	private int port;

	private String adminToken;

	@BeforeEach
	void setUp() {
		log.info("Setting up E2E test environment on port: {} - Database URL: {}", port, postgres.getJdbcUrl());
		RestAssured.baseURI = "http://localhost:" + port;

		Map<String, String> credentials = new HashMap<>();
		credentials.put("username", "admin1");
		credentials.put("password", "admin");

		log.info("Attempting to login with username: admin1");
		log.debug("Login request body: {}", credentials);
		adminToken = given().contentType(ContentType.JSON).body(credentials).when().post("/auth/login").then()
				.statusCode(200).extract().asString();
		log.info("Admin token: {}", adminToken);
	}

	@Test
	void createAndGetTeacher_E2E() {
		log.info("Running createAndGetTeacher_E2E test");
		Map<String, Object> teacherRequest = new HashMap<>();
		teacherRequest.put("teacherName", "Nguyen Van C");
		teacherRequest.put("teacherEmail", "nguyenvanc@university.edu.vn");
		teacherRequest.put("teacherDateOfBirth", "1982-01-01");
		teacherRequest.put("teacherGender", "Male");
		teacherRequest.put("teacherAddress", "789 Hanoi");
		teacherRequest.put("teacherPhoneNumber", "0123456789");
		log.debug("Teacher creation request: {}", teacherRequest);

		given().header("Authorization", "Bearer " + adminToken).contentType(ContentType.JSON).body(teacherRequest)
				.when().post("/api/users/create/teacher").then().statusCode(200)
				.body("teacherName", equalTo("Nguyen Van C"));

		given().header("Authorization", "Bearer " + adminToken).contentType(ContentType.JSON).when()
				.get("/api/users/teacher").then().statusCode(200).body("[0].teacherName", equalTo("Nguyen Van C"));
	}
}