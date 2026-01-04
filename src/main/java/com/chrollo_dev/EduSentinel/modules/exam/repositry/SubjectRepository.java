package com.chrollo_dev.EduSentinel.modules.exam.repositry;

import com.chrollo_dev.EduSentinel.modules.exam.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject,String> {
    Optional<Subject> findByCode(String code);
    Optional<Subject> findByName(String name);
    Optional<Subject> findById(String id);
    boolean existsByCode(String code);
}
