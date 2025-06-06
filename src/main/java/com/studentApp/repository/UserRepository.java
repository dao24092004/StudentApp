package com.studentApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u JOIN FETCH u.role r JOIN FETCH r.permissions WHERE LOWER(u.username) = LOWER(:username)")
	User findByUsername(String username);

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}