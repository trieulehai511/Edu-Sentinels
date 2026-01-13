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
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
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

    public SubmissionResponse submitHomework(SubmissionRequest rq){
        User user = securityUtils.getCurrentUser();
        HomeWork homeWork = homeWorkRepository.findById(rq.getHomeworkId()).orElseThrow(() ->  new AppException(ErrorCode.HOMEWORK_NOT_FOUND));

        Map<Integer, String> correctAnswersMap = homeWork.getContent().stream()
                .collect(Collectors.toMap(HomeWork.QuestionData::getId, HomeWork.QuestionData::getCorrectAnswer));
        Map<Integer, Integer> scoreMap = homeWork.getContent().stream()
                .collect(Collectors.toMap(HomeWork.QuestionData::getId, HomeWork.QuestionData::getScore));
        double totalScore = 0;

        for (Submission.StudentAnswer studentAns : rq.getAnswers()) {
            String correct = correctAnswersMap.get(studentAns.getQuestionId());

            if (correct != null && correct.equals(studentAns.getSelectedOption())) {
                totalScore += scoreMap.getOrDefault(studentAns.getQuestionId(), 0);
            }
        }

        Submission submission = Submission.builder()
                .homeWork(homeWork)
                .student(user)
                .studentAnswers(rq.getAnswers())
                .score(totalScore)
                .build();
        Submission savedSubmission = submissionRepository.save(submission);
        SubmissionResponse response = submissionMapper.toResponse(savedSubmission);
        messagingTemplate.convertAndSend("/topic/homework/" + rq.getHomeworkId(),response);
        return response;
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
        // Lấy tất cả bài nộp của học sinh này
        List<Submission> submissions = submissionRepository.findAllByStudent_IdOrderByCreateAtDesc(studentId);
        return submissions.stream().map(submissionMapper::toResponse).collect(Collectors.toList());
    }
}
