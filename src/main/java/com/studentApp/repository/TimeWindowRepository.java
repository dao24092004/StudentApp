package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.TimeWindow;

public interface TimeWindowRepository extends JpaRepository<TimeWindow, Long> {

	@Query("SELECT tw FROM TimeWindow tw WHERE tw.classId = :classId")
	List<TimeWindow> findByClassId(@Param("classId") Long classId);
}