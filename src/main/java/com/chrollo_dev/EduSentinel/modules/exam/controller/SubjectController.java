package com.chrollo_dev.EduSentinel.modules.exam.controller;

import com.chrollo_dev.EduSentinel.common.dto.APIResponse;
import com.chrollo_dev.EduSentinel.modules.exam.entity.Subject;
import com.chrollo_dev.EduSentinel.modules.exam.service.SubjectService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class SubjectController {
    SubjectService subjectService;

    @PostMapping
    APIResponse<Subject> create(@RequestBody @Valid Subject subject) {
        return APIResponse.<Subject>builder().result(subjectService.create(subject)).build();
    }

    @GetMapping
    APIResponse<List<Subject>> findAll() {
        return APIResponse.<List<Subject>>builder().result(subjectService.findAll()).build();
    }




}
