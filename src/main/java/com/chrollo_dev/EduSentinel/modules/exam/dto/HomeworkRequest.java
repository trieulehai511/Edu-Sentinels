package com.chrollo_dev.EduSentinel.modules.exam.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class HomeworkRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotNull(message = "Phải chọn môn học")
    private String subjectId; // Chỉ cần gửi ID môn học lên

    @NotNull(message = "Nội dung bài tập không được để trống")
    private List<HomeWork.QuestionData> content;
}
