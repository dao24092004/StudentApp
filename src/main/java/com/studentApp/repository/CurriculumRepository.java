package com.studentApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentApp.entity.Curriculum;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
	// This interface extends JpaRepository to provide CRUD operations for
	// ClassGroup entities.
	// You can add custom query methods here if needed.
	Optional<Curriculum> findByCurriculumName(String curriculumName);

}
