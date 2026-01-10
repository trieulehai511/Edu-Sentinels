package com.chrollo_dev.EduSentinel.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    String id;

    @CreatedDate
    @Column(name = "created_at",nullable = false, updatable = false)
    LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updateAt;

    @Column(name = "is_active")
    boolean isActive=true;

}
