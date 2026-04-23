package itsdax.kams.features.user.model.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {}
