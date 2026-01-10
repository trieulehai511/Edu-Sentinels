package com.chrollo_dev.EduSentinel.modules.exam.mapper;

import com.chrollo_dev.EduSentinel.modules.exam.dto.HomeworkDetailResponse;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HomeworkMapper {
    public HomeworkDetailResponse toHomeworkDetailResponse(HomeWork homework) {
        if (homework == null) {
            return null;
        }
        return HomeworkDetailResponse.builder()
                .id(homework.getId())
                .title(homework.getTitle())
                .subjectName(homework.getSubject().getName())
                .content(homework.getContent().stream().map(q -> HomeworkDetailResponse.QuestionDto.builder()
                        .id(q.getId())
                        .question(q.getQuestion())
                        .options(q.getOptions())
                        .score(q.getScore())
                        .build()

                ).collect(Collectors.toList()))
                .build();
    }
}
