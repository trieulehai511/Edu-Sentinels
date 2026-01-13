package com.chrollo_dev.EduSentinel.modules.user.entity;

import com.chrollo_dev.EduSentinel.common.entity.BaseEntity;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Entity
@Table(name = "student_guardians", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"guardian_id", "student_id"}) // Chặn trùng lặp
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentGuardian extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "guardian_id", nullable = false)
    User guardian; // Người dùng có role SUPERVISE

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    User student; // Người dùng có role STUDENT
}
