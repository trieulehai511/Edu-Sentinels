package com.chrollo_dev.EduSentinel.modules.submission.dto;

import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmissionResponse {
    String id;
    Double score;
    LocalDateTime submittedAt;

    UserResponse student;
    String homeworkId;
    String homeworkTitle;
}
