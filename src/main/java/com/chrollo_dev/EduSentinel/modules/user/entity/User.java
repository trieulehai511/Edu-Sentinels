package com.chrollo_dev.EduSentinel.modules.user.entity;

import com.chrollo_dev.EduSentinel.common.entity.BaseEntity;
import com.chrollo_dev.EduSentinel.modules.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true,nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role;
    private String email;

}
