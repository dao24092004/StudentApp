package com.studentApp.e2e;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test") // Kích hoạt profile test để sử dụng application-test.properties
class TeacherApiE2ETest {

	@Container
	private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("test").withUsername("studentApp").withPassword("1234$"); // Cấu hình container PostgreSQL
																						// cho kiểm thử

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl); // Override URL từ Testcontainers
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
		registry.add("spring.profiles.active", () -> "test");
	}

	@LocalServerPort
	private int port; // Cổng ngẫu nhiên cho E2E testing

	private String adminToken;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port; // Đặt base URI cho RestAssured

		Map<String, String> credentials = new HashMap<>();
		credentials.put("username", "admin1");
		credentials.put("password", "admin");

		adminToken = given().contentType(ContentType.JSON).body(credentials).when().post("/auth/login").then()
				.statusCode(200).extract().path("accessToken"); // Lấy token từ response
	}

	@Test
	void createAndGetTeacher_E2E() {
		// Dữ liệu tạo giáo viên
		Map<String, Object> teacherRequest = new HashMap<>();
		teacherRequest.put("teacherName", "Nguyen Van C");
		teacherRequest.put("teacherEmail", "nguyenvanc@university.edu.vn");
		teacherRequest.put("teacherDateOfBirth", "1982-01-01");
		teacherRequest.put("teacherGender", "Male");
		teacherRequest.put("teacherAddress", "789 Hanoi");
		teacherRequest.put("teacherPhoneNumber", "0123456789");

		// Tạo giáo viên
		given().header("Authorization", "Bearer " + adminToken).contentType(ContentType.JSON).body(teacherRequest)
				.when().post("/api/users/create/teacher").then().statusCode(200)
				.body("teacherName", equalTo("Nguyen Van C"));

		// Lấy danh sách giáo viên
		given().header("Authorization", "Bearer " + adminToken).contentType(ContentType.JSON).when()
				.get("/api/users/teacher").then().statusCode(200).body("[0].teacherName", equalTo("Nguyen Van C")); // Kiểm
																													// tra
																													// dữ
																													// liệu
																													// vừa
																													// tạo
	}
}