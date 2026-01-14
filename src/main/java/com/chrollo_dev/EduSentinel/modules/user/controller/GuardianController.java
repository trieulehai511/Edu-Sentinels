package com.chrollo_dev.EduSentinel.modules.user.controller;

import com.chrollo_dev.EduSentinel.common.dto.APIResponse;
import com.chrollo_dev.EduSentinel.common.utils.SecurityUtils;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionResponse;
import com.chrollo_dev.EduSentinel.modules.submission.entity.Submission;
import com.chrollo_dev.EduSentinel.modules.submission.mapper.SubmissionMapper;
import com.chrollo_dev.EduSentinel.modules.submission.repository.SubmissionRepository;
import com.chrollo_dev.EduSentinel.modules.submission.service.SubmissionService;
import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import com.chrollo_dev.EduSentinel.modules.user.entity.StudentGuardian;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import com.chrollo_dev.EduSentinel.modules.user.mapper.UserMapper;
import com.chrollo_dev.EduSentinel.modules.user.repository.StudentGuardianRepository;
import com.chrollo_dev.EduSentinel.modules.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final SubmissionRepository submissionRepository;
    private final SubmissionMapper submissionMapper;
    private final ObjectMapper objectMapper;

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
        return APIResponse.<List<SubmissionResponse>>builder()
                .result(submissionService.getSubmissionsByStudentId(studentId))
                .build();
    }
    @GetMapping("/submission/{submissionId}")
    public APIResponse<SubmissionResponse> getSubmissionDetail(@PathVariable String submissionId) {
        User guardian = securityUtils.getCurrentUser();
        System.out.println("1. Guardian: " + guardian.getUsername()); // Log debug

        // 1. Tìm bài nộp
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài nộp này!"));
        System.out.println("2. Found submission: " + submission.getId());

        // 2. Check quyền
        User student = submission.getStudent();
        boolean isLinked = guardianRepository.existsByGuardian_UsernameAndStudent_Username(
                guardian.getUsername(),
                student.getUsername()
        );

        if (!isLinked) {
            throw new RuntimeException("Bạn không có quyền xem bài này!");
        }
        System.out.println("3. Auth check passed");
        UserResponse studentDto = UserResponse.builder()
                .id(student.getId())
                .username(student.getUsername())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .build();
        List<Map<String, Object>> detailsList = new ArrayList<>();

        try {
            // Thử chuyển đổi từ String JSON sang List
            if (submission.getStudentAnswers() != null) {
                detailsList = objectMapper.readValue(
                        submission.getStudentAnswers().toString(),
                        new TypeReference<List<Map<String, Object>>>(){}
                );
            }
        } catch (Exception e) {
            System.out.println("Lỗi parse JSON: " + e.getMessage());
            // Nếu lỗi thì để list rỗng hoặc xử lý tùy ý
        }
        // Map thông tin bài nộp
        SubmissionResponse response = SubmissionResponse.builder()
                .id(submission.getId())
                .score(submission.getScore())
                .submittedAt(submission.getCreateAt())
                .homeworkTitle(submission.getHomeWork().getTitle())
                .student(studentDto)
                .details(detailsList)
                .build();

        System.out.println("4. Response built successfully");

        return APIResponse.<SubmissionResponse>builder()
                .result(response)
                .build();
    }
}