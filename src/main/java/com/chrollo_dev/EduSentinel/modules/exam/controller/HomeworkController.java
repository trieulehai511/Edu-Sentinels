package com.chrollo_dev.EduSentinel.modules.exam.controller;


import com.chrollo_dev.EduSentinel.common.dto.APIResponse;
import com.chrollo_dev.EduSentinel.modules.exam.dto.HomeworkDetailResponse;
import com.chrollo_dev.EduSentinel.modules.exam.dto.HomeworkRequest;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import com.chrollo_dev.EduSentinel.modules.exam.service.HomeworkService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/homework")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HomeworkController {
    HomeworkService homeworkService;

    @PostMapping
    APIResponse<HomeWork> createHomework(@RequestBody @Valid HomeworkRequest rq){
        return APIResponse.<HomeWork>builder().result(homeworkService.createHomework(rq)).build();
    }

    @PutMapping("/{id}")
    APIResponse<HomeWork> updateHomework(@PathVariable String id,@RequestBody @Valid HomeworkRequest rq){
        return APIResponse.<HomeWork>builder().result(homeworkService.updateHomework(id,rq)).build();
    }
    @GetMapping("/{id}")
    APIResponse<HomeworkDetailResponse> getHomework(@PathVariable String id){
        return APIResponse.<HomeworkDetailResponse>builder().result(homeworkService.getHomeworkDetail(id)).build();
    }
}
