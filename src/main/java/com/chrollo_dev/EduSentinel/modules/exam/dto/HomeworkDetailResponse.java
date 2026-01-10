package com.chrollo_dev.EduSentinel.modules.exam.dto;


import com.chrollo_dev.EduSentinel.modules.exam.entity.Subject;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeworkDetailResponse {

    String id;
    String title;
    String subjectName;
    List<QuestionDto> content;
    @Data
    @Builder
    public static class QuestionDto {
        private int id;
        private String question;
        private List<String> options;
        private int score;

    }
}
