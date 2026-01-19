package com.chrollo_dev.EduSentinel.modules.submission.dto;

import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmissionResponse {
    String id;
    Double score;
    LocalDateTime submittedAt;
    List<Map<String, Object>> details;
    UserResponse student;
    String homeworkId;
    String homeworkTitle;
}
