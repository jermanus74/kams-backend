package itsdax.kams.features.user.mapper.impl;

import itsdax.kams.features.user.mapper.AuthMapper;
import itsdax.kams.features.user.model.dto.CreateUserRequest;
import itsdax.kams.features.user.model.dto.UserResponse;
import itsdax.kams.features.user.model.entity.User;
import org.springframework.stereotype.Component;


@Component
public class AuthMapperImpl implements AuthMapper {

    // =========================
    // DTO → ENTITY
    // =========================
    @Override
    public User fromDto(
            CreateUserRequest dto, String encodedPassword, String regNo) {
        return User.builder()
                .registrationNumber(regNo)
                .email(dto.email())
                .password(encodedPassword) // already encoded
                .role(dto.role())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(dto.phoneNumber())
                .build();
    }

    // =========================
    // ENTITY → DTO
    // =========================
    @Override
    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getRegistrationNumber(),
                user.getEmail(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getProfileImageUrl(),
                user.getLastLoginAt()
        );
    }
}
