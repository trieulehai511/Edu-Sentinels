package com.chrollo_dev.EduSentinel.modules.exam.service;

import com.chrollo_dev.EduSentinel.common.exception.AppException;
import com.chrollo_dev.EduSentinel.common.exception.ErrorCode;
import com.chrollo_dev.EduSentinel.modules.exam.entity.Subject;
import com.chrollo_dev.EduSentinel.modules.exam.repositry.SubjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectService {
    SubjectRepository subjectRepository;

    public Subject create(Subject sj) {
        if(subjectRepository.existsByCode(sj.getCode())){
            throw  new AppException(ErrorCode.SUBJECT_EXISTED);
        }
        Subject subject = Subject.builder()
                .code(sj.getCode())
                .name(sj.getName())
                .description(sj.getDescription())
                .build();
        return  subjectRepository.save(subject);
    }

    public List<Subject> findAll() {
        List<Subject> subjects =  subjectRepository.findAll();
        if(subjects.isEmpty()){
            throw new AppException(ErrorCode.SUBJECT_NOT_EXISTED);
        }
        return subjects;
    }


}
