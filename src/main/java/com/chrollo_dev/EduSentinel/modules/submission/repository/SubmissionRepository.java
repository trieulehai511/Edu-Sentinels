package com.chrollo_dev.EduSentinel.modules.submission.repository;

import com.chrollo_dev.EduSentinel.modules.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository  extends JpaRepository<Submission, String> {
    List<Submission> findAllByStudent_UsernameOrderByCreateAtDesc(String username);
}
