package itsdax.kams.features.user.model.dto;

public record ResetPasswordRequest(
        String registrationNumber,
        String oldPassword,
        String newPassword
) {}
