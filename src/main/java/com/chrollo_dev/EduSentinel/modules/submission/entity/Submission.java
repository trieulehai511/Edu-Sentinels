package com.chrollo_dev.EduSentinel.modules.submission.entity;

import com.chrollo_dev.EduSentinel.common.entity.BaseEntity;
import com.chrollo_dev.EduSentinel.modules.exam.entity.HomeWork;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Submission extends BaseEntity {

    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    User student;

    @ManyToOne
    @JoinColumn(name ="homework_id", nullable = false)
    HomeWork homeWork;

    Double score;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<StudentAnswer>  studentAnswers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public  static class StudentAnswer{
        private int questionId;
        private String selectedOption;
    }


}
