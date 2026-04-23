package itsdax.kams.features.user.model.dto;

public record LoginRequest(
        String registrationNumber,
        String password
) {}
