package itsdax.kams.features.user.model.dto;


import itsdax.kams.features.user.model.enums.Roles;

public record CreateUserRequest(
        String email,
        Roles role,
        String firstName,
        String lastName,
        String phoneNumber
) {}