package itsdax.kams.features.user.model.entity;

import itsdax.kams.core.constants.BaseEntity;
import itsdax.kams.features.user.model.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_reg_no", columnList = "registrationNumber"),
                @Index(name = "idx_user_role", columnList = "role")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, updatable = false)
    private String registrationNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

    // BASIC PROFILE (ALL ROLES)
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;
    private String profileImageUrl;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    private boolean passwordChanged = false;

//    flags
    private LocalDateTime lastLoginAt;
    private boolean forcePasswordReset = false;
    private LocalDateTime passwordChangedAt;
    private int failedAttempts = 0;
    private boolean accountLocked = false;
    private LocalDateTime lockTime;
}