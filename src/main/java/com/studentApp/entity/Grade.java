package com.studentApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_grade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "attendance_score")
    private Double attendanceScore;

    @Column(name = "exam_score")
    private Double examScore;

    @Column(name = "final_score")
    private Double finalScore;

    @Column(name = "note", length = 255)
    private String note;
}
