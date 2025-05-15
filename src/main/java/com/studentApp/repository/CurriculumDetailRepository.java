package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.CurriculumDetail;

public interface CurriculumDetailRepository extends JpaRepository<CurriculumDetail, Long> {
	@Query("SELECT cd FROM CurriculumDetail cd " + "JOIN FETCH cd.curriculum c " + "JOIN FETCH cd.subject s "
			+ "JOIN FETCH cd.semester sm " + "WHERE cd.curriculum.id = :curriculumId")
	List<CurriculumDetail> findByCurriculumIdWithDetails(@Param("curriculumId") Long curriculumId);
}