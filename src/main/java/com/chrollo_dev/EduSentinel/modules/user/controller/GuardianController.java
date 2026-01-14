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
    // private final SubmissionMapper submissionMapper; // Có thể bỏ nếu không dùng nữa
    private final ObjectMapper objectMapper;

    // 1. Kết nối với con
    @PostMapping("/connect")
    public APIResponse<String> connectStudent(@RequestParam String studentUsername) {
        User guardian = securityUtils.getCurrentUser();

        User student = userRepository.findUserByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh này!"));

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

    // 2. Lấy danh sách con
    @GetMapping("/children")
    public APIResponse<List<UserResponse>> getMyChildren() {
        User guardian = securityUtils.getCurrentUser();
        List<StudentGuardian> list = guardianRepository.findAllByGuardian_Username(guardian.getUsername());

        List<UserResponse> children = list.stream()
                .map(link -> userMapper.toUserResponse(link.getStudent()))
                .collect(Collectors.toList());

        return APIResponse.<List<UserResponse>>builder().result(children).build();
    }

    // 3. Lấy lịch sử điểm số
    @GetMapping("/student-history/{studentId}")
    public APIResponse<List<SubmissionResponse>> getChildHistory(@PathVariable String studentId) {
        return APIResponse.<List<SubmissionResponse>>builder()
                .result(submissionService.getSubmissionsByStudentId(studentId))
                .build();
    }

    // 4. Lấy chi tiết bài làm
    @GetMapping("/submission/{submissionId}")
    public APIResponse<SubmissionResponse> getSubmissionDetail(@PathVariable String submissionId) {
        User guardian = securityUtils.getCurrentUser();

        // Log để debug (Xóa sau khi chạy ổn định)
        System.out.println("1. Guardian: " + guardian.getUsername());

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài nộp này!"));
        System.out.println("2. Found submission: " + submission.getId());

        User student = submission.getStudent();
        boolean isLinked = guardianRepository.existsByGuardian_UsernameAndStudent_Username(
                guardian.getUsername(),
                student.getUsername()
        );

        if (!isLinked) {
            throw new RuntimeException("Bạn không có quyền xem bài này!");
        }
        System.out.println("3. Auth check passed");

        // Map User thủ công
        UserResponse studentDto = UserResponse.builder()
                .id(student.getId())
                .username(student.getUsername())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .build();

        // Xử lý JSON Student Answers
        List<Map<String, Object>> detailsList = new ArrayList<>();
        try {

            if (submission.getStudentAnswers() != null) {
                // 👇 DÙNG HÀM NÀY: convertValue (Chuyển từ Object này sang Object khác)
                detailsList = objectMapper.convertValue(
                        submission.getStudentAnswers(),
                        new TypeReference<List<Map<String, Object>>>(){}
                );
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi convert data: " + e.getMessage());
        }

        // Map Response thủ công
        SubmissionResponse response = SubmissionResponse.builder()
                .id(submission.getId())
                .score(submission.getScore())
                // .submittedAt(submission.getCreateAt()) // Kiểm tra lại tên hàm
                .submittedAt(submission.getCreateAt()) // Thường là createdAt
                .homeworkTitle(submission.getHomeWork().getTitle())
                .homeworkId(submission.getHomeWork().getId()) // Thêm ID bài tập nếu cần
                .student(studentDto)
                .details(detailsList) // Gán list chi tiết vào
                .build();

        System.out.println("4. Response built successfully");

        return APIResponse.<SubmissionResponse>builder()
                .result(response)
                .build();
    }
}