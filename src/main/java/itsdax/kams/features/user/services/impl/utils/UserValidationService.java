package itsdax.kams.features.user.services.impl.utils;


import itsdax.kams.core.config.exceptions.InvalidRequestException;
import itsdax.kams.features.user.model.dto.CreateUserRequest;
import itsdax.kams.features.user.model.entity.PasswordHistory;
import itsdax.kams.features.user.model.entity.User;
import itsdax.kams.features.user.repository.PasswordHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidationService {

    private final PasswordHistoryRepo passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;


    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^*&+=]).{8,}$");



    public void saveToHistory(User user) {

        passwordHistoryRepository.save(
                PasswordHistory.builder()
                        .user(user)
                        .password(user.getPassword())
                        .build()
        );
    }

    public void checkPasswordReuse(User user, String rawPassword) {

        List<PasswordHistory> history =
                passwordHistoryRepository.findTop5ByUserOrderByCreatedAtDesc(user);

        for (PasswordHistory h : history) {
            if (passwordEncoder.matches(rawPassword, h.getPassword())) {
                throw new InvalidRequestException("You cannot reuse recent passwords");
            }
        }
    }
    // =========================
    // SIGNUP VALIDATION
    // =========================
    public void validateSignupRequest(CreateUserRequest request) {

        if (request == null) {
            throw new InvalidRequestException("Signup request cannot be null");
        }

        validateCommon(request.email(), request.role());
    }

    // =========================
    // PASSWORD CHANGE VALIDATION
    // =========================
    public void validatePasswordChange(String newPassword) {

        if (newPassword == null || newPassword.isBlank()) {
            throw new InvalidRequestException("Password is required");
        }

        if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new InvalidRequestException(
                    "Password must be at least 8 characters long and contain: " +
                            "1 uppercase letter, 1 lowercase letter, digit, and special character (!@#$%^*&+=)"
            );
        }
    }

    // =========================
    // SHARED VALIDATION LOGIC
    // =========================
    private void validateCommon(String email, Object role) {

        if (email == null || email.isBlank()) {
            throw new InvalidRequestException("Email is required");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidRequestException("Invalid email format");
        }

//        if (password == null || password.isBlank()) {
//            throw new InvalidRequestException("Password is required");
//        }

//        if (!PASSWORD_PATTERN.matcher(password).matches()) {
//            throw new InvalidRequestException(
//                    "Password must be at least 8 characters long and contain: " +
//                            "1 uppercase letter, 1 lowercase letter, digit, and special character (!@#$%^*&+=)"
//            );
//        }

        if (role == null) {
            throw new InvalidRequestException("Role is required");
        }
    }
}
