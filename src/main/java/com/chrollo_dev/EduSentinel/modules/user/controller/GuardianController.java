package com.chrollo_dev.EduSentinel.modules.user.controller;

import com.chrollo_dev.EduSentinel.common.dto.APIResponse;
import com.chrollo_dev.EduSentinel.common.utils.SecurityUtils;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionResponse;
import com.chrollo_dev.EduSentinel.modules.submission.service.SubmissionService;
import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import com.chrollo_dev.EduSentinel.modules.user.entity.StudentGuardian;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import com.chrollo_dev.EduSentinel.modules.user.mapper.UserMapper;
import com.chrollo_dev.EduSentinel.modules.user.repository.StudentGuardianRepository;
import com.chrollo_dev.EduSentinel.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/guardian")
@RequiredArgsConstructor
public class GuardianController {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final StudentGuardianRepository guardianRepository;
    private final SubmissionService submissionService;
    private final UserMapper userMapper;

    // 1. Kết nối với con (Nhập username của con)
    @PostMapping("/connect")
    public APIResponse<String> connectStudent(@RequestParam String studentUsername) {
        User guardian = securityUtils.getCurrentUser();

        // Tìm học sinh
        User student = userRepository.findUserByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh này!"));

        // Check trùng
        if (guardianRepository.existsByGuardian_UsernameAndStudent_Username(guardian.getUsername(), studentUsername)) {
            throw new RuntimeException("Đã theo dõi học sinh này rồi!");
        }

        StudentGuardian link = StudentGuardian.builder()
                .guardian(guardian)
                .student(student)
                .build();
        guardianRepository.save(link);

        return APIResponse.<String>builder().result("Kết nối thành công!").build();
    }

    // 2. Lấy danh sách con đang theo dõi
    @GetMapping("/children")
    public APIResponse<List<UserResponse>> getMyChildren() {
        User guardian = securityUtils.getCurrentUser();
        List<StudentGuardian> list = guardianRepository.findAllByGuardian_Username(guardian.getUsername());

        List<UserResponse> children = list.stream()
                .map(link -> userMapper.toUserResponse(link.getStudent()))
                .collect(Collectors.toList());

        return APIResponse.<List<UserResponse>>builder().result(children).build();
    }

    // 3. Lấy lịch sử điểm số của con
    @GetMapping("/student-history/{studentId}")
    public APIResponse<List<SubmissionResponse>> getChildHistory(@PathVariable String studentId) {
        // Cần thêm hàm này vào SubmissionService như hướng dẫn bài trước
        return APIResponse.<List<SubmissionResponse>>builder()
                .result(submissionService.getSubmissionsByStudentId(studentId))
                .build();
    }
}