package com.chrollo_dev.EduSentinel.modules.exam.entity;

import com.chrollo_dev.EduSentinel.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "subjects")
public class Subject extends BaseEntity {

    @Column(nullable = false)
    @NotBlank(message = "Tên")
    String name;
    String description;

    @Column(name = "code", unique = true, nullable = false)
    String code;
}
