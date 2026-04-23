package itsdax.kams.features.user.services;

import itsdax.kams.features.user.model.dto.*;

public interface AuthService  {
    AuthResponse activate(ActivateAccountRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse resetPassword(ResetPasswordRequest request);
    void logout(String token);
}

