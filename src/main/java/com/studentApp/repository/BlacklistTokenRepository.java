package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.BlacklistToken;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
	boolean existsByToken(String token);
}