package com.chrollo_dev.EduSentinel.modules.submission.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmissionDetailResponse {
    String id;
    Double score;
    String homeworkTitle;

    List<QuestionResult> details;
    @Data
    @Builder
    public static class QuestionResult {
        private int questionId;
        private String questionText;
        private List<String> options;     // Để hiển thị lại các lựa chọn
        private String selectedOption;    // Học sinh chọn: "A"
        private String correctOption;     // Đáp án đúng: "B"
        private boolean isCorrect;        // Kết quả: false
        private Double score;                // Điểm câu này: 5
    }
}
