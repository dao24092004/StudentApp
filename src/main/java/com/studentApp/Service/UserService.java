package com.studentApp.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.Entity.User;
import com.studentApp.Repository.UserRepository;
import com.studentApp.dto.request.UserCreationRequest;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User createUser(UserCreationRequest resquest) {
		User user = new User();
		user.setId(resquest.getId());
		user.setEmail(resquest.getEmail());
		user.setUsername(resquest.getUsername());
		user.setRoleId(resquest.getRoleId());
		user.setCreatedAt(null);
		user.setUpdatedAt(null);

		user.setPassword(resquest.getPassword());

		return userRepository.save(user);

	}

	public User getUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public User updateUser(UserCreationRequest request, int id) {
		User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			return null;
		}
		user.setEmail(request.getEmail());
		user.setUsername(request.getUsername());
		user.setRoleId(request.getRoleId());
		user.setUpdatedAt(null);
		user.setPassword(request.getPassword());

		return userRepository.save(user);
	}

}
