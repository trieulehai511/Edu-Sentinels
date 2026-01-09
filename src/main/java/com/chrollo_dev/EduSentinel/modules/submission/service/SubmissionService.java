package com.chrollo_dev.EduSentinel.modules.submission.service;

import com.chrollo_dev.EduSentinel.common.exception.AppException;
import com.chrollo_dev.EduSentinel.common.exception.ErrorCode;
import com.chrollo_dev.EduSentinel.common.utils.SecurityUtils;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import com.chrollo_dev.EduSentinel.modules.exam.repositry.HomeWorkRepository;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionRequest;
import com.chrollo_dev.EduSentinel.modules.submission.entity.Submission;
import com.chrollo_dev.EduSentinel.modules.submission.repository.SubmissionRepository;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubmissionService {
    HomeWorkRepository homeWorkRepository;
    SubmissionRepository submissionRepository;
    SecurityUtils securityUtils;

    public Submission submitHomework(SubmissionRequest rq){
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

        return submissionRepository.save(submission);
    }
}
