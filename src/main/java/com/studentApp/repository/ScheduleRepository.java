package com.studentApp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id = :classId")
	List<Schedule> findByClassId(@Param("classId") Long classId);

	@Query("SELECT s FROM Schedule s WHERE DATE(s.startTime) = :date")
	List<Schedule> findByDate(@Param("date") LocalDate date);

	@Query("SELECT s FROM Schedule s WHERE DATE(s.startTime) BETWEEN :startDate AND :endDate")
	List<Schedule> findByWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id = :classId AND DATE(s.startTime) = :date")
	List<Schedule> findByClassIdAndDate(@Param("classId") Long classId, @Param("date") LocalDate date);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id = :classId AND DATE(s.startTime) BETWEEN :startDate AND :endDate")
	List<Schedule> findByClassIdAndWeek(@Param("classId") Long classId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.teacher.id = :teacherId AND DATE(s.startTime) = :date")
	List<Schedule> findByTeacherIdAndDate(@Param("teacherId") Long teacherId, @Param("date") LocalDate date);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.teacher.id = :teacherId AND DATE(s.startTime) BETWEEN :startDate AND :endDate")
	List<Schedule> findByTeacherIdAndWeek(@Param("teacherId") Long teacherId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id IN (SELECT c.id FROM Class c JOIN Student st ON st.classGroup.id = c.classGroup.id WHERE st.id = :studentId) AND DATE(s.startTime) = :date")
	List<Schedule> findByStudentIdAndDate(@Param("studentId") Long studentId, @Param("date") LocalDate date);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id IN (SELECT c.id FROM Class c JOIN Student st ON st.classGroup.id = c.classGroup.id WHERE st.id = :studentId) AND DATE(s.startTime) BETWEEN :startDate AND :endDate")
	List<Schedule> findByStudentIdAndWeek(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id IN (SELECT c.id FROM Class c JOIN Student st ON st.classGroup.id = c.classGroup.id WHERE st.id = :studentId) AND s.semester.id = :semesterId")
	List<Schedule> findByStudentIdAndSemesterId(@Param("studentId") Long studentId,
			@Param("semesterId") Long semesterId);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.teacher.id = :teacherId AND s.semester.id = :semesterId")
	List<Schedule> findByTeacherIdAndSemesterId(@Param("teacherId") Long teacherId,
			@Param("semesterId") Long semesterId);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id IN (SELECT c.id FROM Class c JOIN Student st ON st.classGroup.id = c.classGroup.id WHERE st.id = :studentId) AND s.semester.id = :semesterId AND s.week = :week")
	List<Schedule> findByStudentIdAndSemesterIdAndWeek(@Param("studentId") Long studentId,
			@Param("semesterId") Long semesterId, @Param("week") Integer week);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.teacher.id = :teacherId AND s.semester.id = :semesterId AND s.week = :week")
	List<Schedule> findByTeacherIdAndSemesterIdAndWeek(@Param("teacherId") Long teacherId,
			@Param("semesterId") Long semesterId, @Param("week") Integer week);

	@Query("SELECT s FROM Schedule s WHERE s.classEntity.id = :classId AND s.week = :week")
	List<Schedule> findByClassIdAndWeekNumber(@Param("classId") Long classId, @Param("week") Integer week);
}
