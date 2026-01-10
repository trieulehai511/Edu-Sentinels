package com.chrollo_dev.EduSentinel.modules.submission.mapper;

import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionDetailResponse;
import com.chrollo_dev.EduSentinel.modules.submission.dto.SubmissionResponse;
import com.chrollo_dev.EduSentinel.modules.submission.entity.Submission;
import com.chrollo_dev.EduSentinel.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubmissionMapper {
    private final UserMapper userMapper;
    public SubmissionResponse toResponse(Submission entity) {
        if (entity == null) return null;

        return SubmissionResponse.builder()
                .id(entity.getId())
                .score(entity.getScore())
                .submittedAt(entity.getCreateAt())

                .student(userMapper.toUserResponse(entity.getStudent()))
                .homeworkId(entity.getHomeWork().getId())
                .homeworkTitle(entity.getHomeWork().getTitle())
                .build();
    }
    public SubmissionDetailResponse toDetailResponse(Submission submission) {
        HomeWork homework = submission.getHomeWork();
        List<Submission.StudentAnswer> studentAnswers = submission.getStudentAnswers();

        Map<Integer, String> studentAnswerMap = studentAnswers.stream()
                .collect(Collectors.toMap(Submission.StudentAnswer::getQuestionId, Submission.StudentAnswer::getSelectedOption));

        List<SubmissionDetailResponse.QuestionResult> details = new ArrayList<>();

        for (HomeWork.QuestionData question : homework.getContent()) {
            String userSelected = studentAnswerMap.get(question.getId()); // Học sinh chọn gì?
            String correctAnswer = question.getCorrectAnswer();           // Đáp án đúng là gì?

            boolean isCorrect = correctAnswer.equals(userSelected);

            details.add(SubmissionDetailResponse.QuestionResult.builder()
                    .questionId(question.getId())
                    .questionText(question.getQuestion())
                    .options(question.getOptions())
                    .selectedOption(userSelected)   // Cái user chọn
                    .correctOption(correctAnswer)   // Cái user nên chọn
                    .isCorrect(isCorrect)           // Đúng hay sai
                    .score(question.getScore())
                    .build());
        }

        return SubmissionDetailResponse.builder()
                .id(submission.getId())
                .score(submission.getScore())
                .homeworkTitle(homework.getTitle())
                .details(details)
                .build();
    }
}
