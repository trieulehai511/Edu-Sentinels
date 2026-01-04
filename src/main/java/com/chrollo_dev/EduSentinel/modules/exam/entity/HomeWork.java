package com.chrollo_dev.EduSentinel.modules.exam.entity;

import com.chrollo_dev.EduSentinel.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "homeworks")
public class HomeWork extends BaseEntity {

    @Column(nullable = false)
    String title;

    @ManyToOne
    @JoinColumn(nullable = false, name = "subject_id")
    Subject subject;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<QuestionData> content;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionData implements Serializable {
        private int id;             // Câu số 1
        private String question;    // Nội dung câu hỏi
        private List<String> options; // ["A. 1", "B. 2", "C. 3"]
        private String correctAnswer; // Đáp án đúng: "A"
        private int score;          // Điểm số: 10
    }
}
