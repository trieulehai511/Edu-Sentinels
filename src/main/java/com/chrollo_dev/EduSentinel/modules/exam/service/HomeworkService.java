package com.chrollo_dev.EduSentinel.modules.exam.service;


import com.chrollo_dev.EduSentinel.common.exception.AppException;
import com.chrollo_dev.EduSentinel.common.exception.ErrorCode;
import com.chrollo_dev.EduSentinel.modules.exam.dto.HomeworkDetailResponse;
import com.chrollo_dev.EduSentinel.modules.exam.dto.HomeworkRequest;
import com.chrollo_dev.EduSentinel.modules.exam.dto.HomeworkResponse;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import com.chrollo_dev.EduSentinel.modules.exam.entity.Subject;
import com.chrollo_dev.EduSentinel.modules.exam.mapper.HomeworkMapper;
import com.chrollo_dev.EduSentinel.modules.exam.repositry.HomeWorkRepository;
import com.chrollo_dev.EduSentinel.modules.exam.repositry.SubjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HomeworkService {
    HomeWorkRepository homeWorkRepository;
    SubjectRepository subjectRepository;
    HomeworkMapper homeworkMapper;
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
    public HomeWork updateHomework(String homeworkId, HomeworkRequest rq) {
        HomeWork homeWork = homeWorkRepository.findById(homeworkId).orElseThrow(()-> new AppException(ErrorCode.HOMEWORK_NOT_FOUND));

        if(!homeWork.getSubject().getId().equals(rq.getSubjectId())) {
            Subject newSubject = subjectRepository.findById(rq.getSubjectId()).orElseThrow(()-> new AppException(ErrorCode.SUBJECT_NOT_EXISTED));
            homeWork.setSubject(newSubject);
        }
        homeWork.setTitle(rq.getTitle());
        homeWork.setContent(rq.getContent());
        return homeWorkRepository.save(homeWork);
    }

    public HomeworkDetailResponse getHomeworkDetail(String homeworkId) {
        HomeWork homeWork = homeWorkRepository.findById(homeworkId).orElseThrow(()-> new AppException(ErrorCode.HOMEWORK_NOT_FOUND));
        return homeworkMapper.toHomeworkDetailResponse(homeWork);
    }

    public List<HomeworkResponse> getAllHomeWorks(String subjectId){
        List<HomeWork> homeWorks;
        if(subjectId != null){
            homeWorks = homeWorkRepository.findAllBySubject_Id(subjectId);
        }else{
            homeWorks = homeWorkRepository.findAll();
        }
        return homeWorks.stream().map(homeworkMapper::toHomeWorkResponse).collect(Collectors.toList());
    }

}
