package com.studentApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentApp.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u JOIN FETCH u.role r JOIN FETCH r.permissions WHERE LOWER(u.username) = LOWER(:username)")
	User findByUsername(String username);

	User findByEmail(String email);
}