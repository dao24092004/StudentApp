package com.studentApp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.Entity.User;
import com.studentApp.Service.UserService;
import com.studentApp.dto.request.UserCreationRequest;

@RestController // Đổi từ @Controller sang @RestController
@RequestMapping("/api/users") // Thêm tiền tố URL để dễ quản lý
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public User createUser(@RequestBody UserCreationRequest request) {
		return userService.createUser(request);
	}

	@GetMapping("/get/{id}") // Thêm phương thức get
	public User getUserById(@PathVariable int id) {
		return userService.getUserById(id);
	}

	@GetMapping("/getall") // Thêm phương thức get
	public List<User> getAllUser() {
		return userService.getAllUser();
	}

	@PostMapping("/update/{id}") // Thêm phương thức post
	public User updateUser(@RequestBody UserCreationRequest request, @PathVariable int id) {
		return userService.updateUser(request, id);
	}
}
