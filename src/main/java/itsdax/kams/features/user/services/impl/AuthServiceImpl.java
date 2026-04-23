package itsdax.kams.features.user.services.impl;

import itsdax.kams.core.config.jwt.JwtService;
import itsdax.kams.core.config.exceptions.auth.*;
import itsdax.kams.features.user.mapper.AuthMapper;
import itsdax.kams.features.user.model.dto.*;
import itsdax.kams.features.user.model.entity.User;
import itsdax.kams.features.user.repository.UserRepository;
import itsdax.kams.features.user.services.AuthService;
import itsdax.kams.features.user.services.impl.utils.UserValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j                  // FIX #4: replaced System.out.println with proper logger
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final UserValidationService validationService;

    // =========================
    // ACTIVATE ACCOUNT
    // =========================
    @Override
    public AuthResponse activate(ActivateAccountRequest request) {

        User user = userRepository.findByRegistrationNumber(request.registrationNumber())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.temporaryPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid temporary password");
        }

        validationService.validatePasswordChange(request.newPassword());
        validationService.checkPasswordReuse(user, request.newPassword());
        validationService.saveToHistory(user);

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setEnabled(true);
        user.setPasswordChanged(true);
        user.setPasswordChangedAt(LocalDateTime.now());

        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, authMapper.toDto(user));
    }

    // =========================
    // LOGIN
    // =========================
    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByRegistrationNumber(request.registrationNumber())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        // =========================
        // ACCOUNT LOCK CHECK
        // =========================
        if (user.isAccountLocked()) {

            if (user.getLockTime() != null &&
                    user.getLockTime().plusMinutes(30).isBefore(LocalDateTime.now())) {

                // Lock window has passed — unlock and allow the login attempt to continue
                user.setAccountLocked(false);
                user.setFailedAttempts(0);
                userRepository.saveAndFlush(user);

            } else {
                throw new AccountLockedException("Account is locked. Try again later.");
            }
        }

        // =========================
        // FORCE PASSWORD RESET
        // =========================
        if (user.isForcePasswordReset()) {
            throw new PasswordResetRequiredException("Password reset required before login");
        }

        // =========================
        // ACTIVATION CHECK
        // =========================
        if (!user.isEnabled()) {
            throw new AccountNotActivatedException("Account not activated");
        }

        // =========================
        // PASSWORD CHECK
        // =========================
        boolean passwordMatches = passwordEncoder.matches(request.password(), user.getPassword());

        if (!passwordMatches) {

            handleFailedLogin(user);
//System.out.println("Failed attempts: ";
            throw new InvalidCredentialsException(
                    "Invalid credentials. Attempts: "   // FIX #1: use local var, not user.getFailedAttempts()
            );
        }

        // =========================
        // PASSWORD EXPIRY CHECK
        // =========================
        checkPasswordExpiry(user);

        user.setFailedAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, authMapper.toDto(user));
    }

    // =========================
    // RESET PASSWORD
    // =========================
    @Override
    public AuthResponse resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByRegistrationNumber(request.registrationNumber())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid current password");
        }

        validationService.validatePasswordChange(request.newPassword());
        validationService.checkPasswordReuse(user, request.newPassword());
        validationService.saveToHistory(user);

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setForcePasswordReset(false);
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setFailedAttempts(0);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, authMapper.toDto(user));
    }

    // =========================
    // LOGOUT
    // =========================
    @Override
    public void logout(String token) {

        jwtService.invalidateToken(token);
        SecurityContextHolder.clearContext();

        // An attacker with log access could reuse it until it expires.
        log.info("Token invalidated successfully");
    }

    // =========================
    // FAILED LOGIN HANDLER
    // =========================
    private void handleFailedLogin(User user) {
        int attempts = user.getFailedAttempts();
        user.setFailedAttempts(attempts);
        attempts++;

        System.out.println("Failed attempts: " + attempts);
        if (attempts >= 5) {
            user.setAccountLocked(true);
            user.setLockTime(LocalDateTime.now());
        }

        userRepository.save(user);
    }

    // =========================
    // PASSWORD EXPIRY CHECK
    // =========================
    private void checkPasswordExpiry(User user) {

        if (user.getPasswordChangedAt() == null) return;

        LocalDateTime expiryDate = user.getPasswordChangedAt().plusDays(90);

        if (LocalDateTime.now().isAfter(expiryDate)) {
            user.setForcePasswordReset(true);
            userRepository.save(user);

            throw new PasswordExpiredException("Password expired. Please reset your password.");
        }
    }
//    to be fixed

}