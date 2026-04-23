package itsdax.kams.features.user.services.impl.utils;

import itsdax.kams.features.user.model.entity.User;
import itsdax.kams.features.user.model.enums.Roles;
import itsdax.kams.features.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserServiceContext {

    private final UserRepository userRepository;

    public UserServiceContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public Long getCurrentUserId() {
        Object principal =
                SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getClaim("userId");
        }

        throw new RuntimeException("Login required");
    }
    public boolean isAdmin(User user) {
        return user.getRole() == Roles.ADMIN;
    }

    public boolean isInstructor(User user) {
        return user.getRole() == Roles.INSTRUCTOR;
    }

    public boolean isStudent(User user) {
        return user.getRole() == Roles.STUDENT;
    }
    public boolean isOperator(User user) {
        return user.getRole() == Roles.STATION_OPERATOR;
    }
    public boolean isRepairman(User user) {
        return user.getRole() == Roles.REPAIRMAN;
    }

}
