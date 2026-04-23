package itsdax.kams.features.user.model.dto;

import itsdax.kams.features.user.model.enums.Roles;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String registrationNumber,
        String email,
        Roles role,
        String firstName,
        String lastName,
        String phoneNumber,
        String profileImageUrl,
        LocalDateTime lastLoginAt
) {}
