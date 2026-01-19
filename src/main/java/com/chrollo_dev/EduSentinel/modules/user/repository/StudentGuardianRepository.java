package com.chrollo_dev.EduSentinel.modules.user.repository;

import com.chrollo_dev.EduSentinel.modules.user.entity.StudentGuardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentGuardianRepository extends JpaRepository<StudentGuardian, String> {
    List<StudentGuardian> findAllByGuardian_Username(String guardianUsername);
    List<StudentGuardian> findAllByStudent_Id(String id);
    boolean existsByGuardian_UsernameAndStudent_Username(String guardian, String student);
}
