package com.chrollo_dev.EduSentinel.modules.exam.repositry;

import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeWorkRepository extends JpaRepository<HomeWork,String> {
}
