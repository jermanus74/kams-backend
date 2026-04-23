package itsdax.kams.features.user.model.dto;

public record ActivateAccountRequest(
        String registrationNumber,
        String temporaryPassword,
        String newPassword
) {}
