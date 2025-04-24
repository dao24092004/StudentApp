package com.studentApp.entity;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tbl_semester")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;
}
=======
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_semester")
@Data
public class Semester {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;
}
>>>>>>> 3a5c06b4e38ad6658f19fd2bb24e9c743daf733b
