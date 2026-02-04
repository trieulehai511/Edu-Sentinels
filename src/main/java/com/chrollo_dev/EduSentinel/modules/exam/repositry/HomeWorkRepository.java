package com.chrollo_dev.EduSentinel.modules.exam.repositry;

import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface HomeWorkRepository extends JpaRepository<HomeWork,String> {
    List<HomeWork> findAllBySubject_Id(String subjectId);

    @Query(value = "SELECT h FROM HomeWork h JOIN FETCH h.subject",
            countQuery = "SELECT COUNT(h) FROM HomeWork h")
    Page<HomeWork> findAllWithSubject(Pageable pageable);

    @Query(value = "SELECT h FROM HomeWork h JOIN FETCH h.subject WHERE h.subject.id = :subjectId",
            countQuery = "SELECT COUNT(h) FROM HomeWork h WHERE h.subject.id = :subjectId")
    Page<HomeWork> findAllBySubjectIdWithSubject(String subjectId, Pageable pageable);
}
