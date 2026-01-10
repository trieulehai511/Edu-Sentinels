package com.chrollo_dev.EduSentinel.modules.exam.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeworkResponse {
    String id;
    String title;
    String subjectName;
    String subjectId;
}
