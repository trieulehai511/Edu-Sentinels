package com.chrollo_dev.EduSentinel.modules.submission.service;

import com.chrollo_dev.EduSentinel.common.exception.AppException;
import com.chrollo_dev.EduSentinel.common.exception.ErrorCode;
import com.chrollo_dev.EduSentinel.common.utils.SecurityUtils;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import com.chrollo_dev.EduSentinel.modules.exam.repositry.HomeWorkRepository;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionDetailResponse;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionRequest;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionResponse;
import com.chrollo_dev.EduSentinel.modules.submission.entity.Submission;
import com.chrollo_dev.EduSentinel.modules.submission.mapper.SubmissionMapper;
import com.chrollo_dev.EduSentinel.modules.submission.repository.SubmissionRepository;
import com.chrollo_dev.EduSentinel.modules.user.entity.StudentGuardian;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import com.chrollo_dev.EduSentinel.modules.user.repository.StudentGuardianRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubmissionService {
    HomeWorkRepository homeWorkRepository;
    SubmissionRepository submissionRepository;
    SubmissionMapper submissionMapper;
    SecurityUtils securityUtils;
    SimpMessagingTemplate messagingTemplate;
    StudentGuardianRepository guardianRepository;
    public SubmissionResponse submitHomework(SubmissionRequest rq) {
        User user = securityUtils.getCurrentUser();
        HomeWork homeWork = homeWorkRepository.findById(rq.getHomeworkId())
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND));

        // 1. Chuẩn bị Map để tra cứu nhanh (O(1))
        Map<Integer, String> correctAnswersMap = homeWork.getContent().stream()
                .collect(Collectors.toMap(HomeWork.QuestionData::getId, HomeWork.QuestionData::getCorrectAnswer));
        Map<Integer, Integer> scoreMap = homeWork.getContent().stream()
                .collect(Collectors.toMap(HomeWork.QuestionData::getId, HomeWork.QuestionData::getScore));

        // 2. Tính điểm thô (Raw Score) - CHỈ TÍNH 1 LẦN DUY NHẤT
        double studentRawScore = 0;
        for (Submission.StudentAnswer studentAns : rq.getAnswers()) {
            String correct = correctAnswersMap.get(studentAns.getQuestionId());

            // Null check & So sánh an toàn
            if (correct != null && correct.trim().equalsIgnoreCase(studentAns.getSelectedOption().trim())) {
                studentRawScore += scoreMap.getOrDefault(studentAns.getQuestionId(), 0);
            }
        }

        // 3. Tính tổng điểm Max của đề
        double maxPossibleScore = homeWork.getContent().stream()
                .mapToDouble(HomeWork.QuestionData::getScore)
                .sum();

        // 4. Quy đổi sang thang 10 (Normalize)
        double finalScore = 0;
        if (maxPossibleScore > 0) {
            finalScore = (studentRawScore / maxPossibleScore) * 10;
        }
        // Làm tròn 2 chữ số thập phân
        finalScore = Math.round(finalScore * 100.0) / 100.0;

        // 5. Lưu xuống DB
        Submission submission = Submission.builder()
                .homeWork(homeWork)
                .student(user)
                .studentAnswers(rq.getAnswers())
                .score(finalScore) // Lưu điểm hệ 10
                .build();
        Submission savedSubmission = submissionRepository.save(submission);

        // 6. Gửi thông báo cho Phụ huynh
        List<StudentGuardian> guardians = guardianRepository.findAllByStudent_Id(user.getId());
        for (StudentGuardian g : guardians) {
            String guardianUsername = g.getGuardian().getUsername();
            messagingTemplate.convertAndSendToUser(
                    guardianUsername,
                    "/queue/notifications",
                    "Con bạn vừa nộp bài " + homeWork.getTitle() + " - Điểm: " + finalScore + "/10"
            );
        }

        return submissionMapper.toResponse(savedSubmission);
    }

    public List<SubmissionResponse> getMySubmissions(){
        User user = securityUtils.getCurrentUser();
        List<Submission> submissions = submissionRepository.findAllByStudent_UsernameOrderByCreateAtDesc(user.getUsername());
        return submissions.stream().map(submissionMapper::toResponse).collect(Collectors.toList());
    }

    public SubmissionDetailResponse getSubmissionDetail(String id) {

        User currentUser = securityUtils.getCurrentUser();

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài nộp"));

        if (!submission.getStudent().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền xem kết quả bài thi này");
        }
        return submissionMapper.toDetailResponse(submission);
    }
    public List<SubmissionResponse> getSubmissionsByHomework(String homeworkId) {
        List<Submission> submissions = submissionRepository.findAllByHomeWork_IdOrderByScoreDesc(homeworkId);
        return submissions.stream().map(submissionMapper::toResponse).collect(Collectors.toList());
    }
    public List<SubmissionResponse> getSubmissionsByStudentId(String studentId) {
        List<Submission> submissions = submissionRepository.findAllByStudent_IdOrderByCreateAtDesc(studentId);
        return submissions.stream().map(submissionMapper::toResponse).collect(Collectors.toList());
    }
}
