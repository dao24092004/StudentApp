package com.studentApp.repository;

import com.studentApp.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    // Bạn có thể thêm các truy vấn tùy chỉnh nếu cần sau này
}
