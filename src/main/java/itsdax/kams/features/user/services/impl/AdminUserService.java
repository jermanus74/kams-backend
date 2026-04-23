package itsdax.kams.features.user.services.impl;

import itsdax.kams.core.config.exceptions.DuplicateResourceException;
import itsdax.kams.core.util.PasswordGenerator;
import itsdax.kams.features.user.mapper.AuthMapper;
import itsdax.kams.features.user.model.dto.CreateUserRequest;
import itsdax.kams.features.user.model.dto.UserResponse;
import itsdax.kams.features.user.model.entity.RegistrationUtil;
import itsdax.kams.features.user.model.entity.User;
import itsdax.kams.features.user.repository.UserRepository;
import itsdax.kams.features.user.services.impl.utils.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserValidationService validationService;


    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "Email already exists",
                    "EMAIL_ALREADY_EXISTS"
            );
        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new DuplicateResourceException(
                    "Phone number already exists",
                    "PHONE_ALREADY_EXISTS"
            );
        }

        validationService.validateSignupRequest(request);
        String regNo = RegistrationUtil.generate(request.role());

        // 🔐 generate temporary password
        String tempPassword = PasswordGenerator.generateTempPassword();
        String encoded = passwordEncoder.encode(tempPassword);

        User user = User.builder()
                .registrationNumber(regNo)
                .email(request.email())
                .password(encoded)
                .role(request.role())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .enabled(false)
                .passwordChanged(false)
                .build();

        userRepository.save(user);

        // 👉 In production: send via email/SMS
        System.out.println("TEMP PASSWORD: " + tempPassword);
        System.out.println("REG NO: " + regNo);

        return authMapper.toDto(user);
    }
}
