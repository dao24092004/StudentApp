package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentApp.entity.TimeWindow;
import com.studentApp.entity.TimeWindowId;

public interface TimeWindowRepository extends JpaRepository<TimeWindow, TimeWindowId> {
}