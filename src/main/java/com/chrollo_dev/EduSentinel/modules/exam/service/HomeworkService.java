package com.chrollo_dev.EduSentinel.modules.exam.service;


import com.chrollo_dev.EduSentinel.modules.exam.dto.HomeworkRequest;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import com.chrollo_dev.EduSentinel.modules.exam.entity.Subject;
import com.chrollo_dev.EduSentinel.modules.exam.repositry.HomeWorkRepository;
import com.chrollo_dev.EduSentinel.modules.exam.repositry.SubjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HomeworkService {
    HomeWorkRepository homeWorkRepository;
    SubjectRepository subjectRepository;

    public HomeWork createHomework(HomeworkRequest rq) {
        Subject subject = subjectRepository.findById(rq.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học với ID này"));

        HomeWork homework = HomeWork.builder()
                .title(rq.getTitle())
                .subject(subject)
                .content(rq.getContent())
                .build();
        return homeWorkRepository.save(homework);
    }
}
