package com.chrollo_dev.EduSentinel.modules.submission.dto;

import com.chrollo_dev.EduSentinel.modules.submission.entity.Submission;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmissionRequest {

    @NotNull
    String homeworkId;

    @NotNull
    private List<Submission.StudentAnswer> answers;
}
