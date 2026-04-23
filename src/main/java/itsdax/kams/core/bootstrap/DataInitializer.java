package itsdax.kams.core.bootstrap;


import itsdax.kams.features.user.model.entity.RegistrationUtil;
import itsdax.kams.features.user.model.entity.User;
import itsdax.kams.features.user.model.enums.Roles;
import itsdax.kams.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        boolean adminExists = userRepository.existsByEmail("admin@kams.com");

        if (!adminExists) {

            User admin = User.builder()
                    .registrationNumber(RegistrationUtil.generate(Roles.ADMIN))
                    .email("admin@kams.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Roles.ADMIN)
                    .firstName("System")
                    .lastName("Admin")
                    .enabled(true)
                    .passwordChanged(true)
                    .build();

            userRepository.save(admin);

            System.out.println("✅ DEFAULT ADMIN CREATED");
        }
    }
}
