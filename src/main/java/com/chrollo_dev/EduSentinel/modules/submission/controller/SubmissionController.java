package com.chrollo_dev.EduSentinel.modules.submission.controller;

import com.chrollo_dev.EduSentinel.common.dto.APIResponse;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionDetailResponse;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionRequest;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionResponse;
import com.chrollo_dev.EduSentinel.modules.submission.entity.Submission;
import com.chrollo_dev.EduSentinel.modules.submission.service.SubmissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/submissions")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubmissionController {

    SubmissionService submissionService;

    @PostMapping
    public APIResponse<SubmissionResponse> submit(@RequestBody SubmissionRequest request) {
        return APIResponse.<SubmissionResponse>builder()
                .result(submissionService.submitHomework(request))
                .build();
    }
    @GetMapping("/my-history")
    public APIResponse<List<SubmissionResponse>> getMyHistory() {
        return APIResponse.<List<SubmissionResponse>>builder()
                .result(submissionService.getMySubmissions())
                .build();
    }
    @GetMapping("/{id}")
    public APIResponse<SubmissionDetailResponse> getSubmissionDetail(@PathVariable String id) {
        return APIResponse.<SubmissionDetailResponse>builder()
                .result(submissionService.getSubmissionDetail(id))
                .build();
    }
}
