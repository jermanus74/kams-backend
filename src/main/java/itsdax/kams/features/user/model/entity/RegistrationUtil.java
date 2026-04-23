package itsdax.kams.features.user.model.entity;

import itsdax.kams.features.user.model.enums.Roles;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistrationUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(Roles role) {

        // 1. YEAR (last 2 digits)
        String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);

        // 2. ROLE CODE
        String roleCode = switch (role) {
            case STUDENT -> "100";
            case INSTRUCTOR -> "200";
            case ADMIN -> "300";
            case STATION_OPERATOR -> "400";
            case REPAIRMAN -> "500";
        };

        // 3. TIME (HHmmss)
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HHmmss"));

        // 4. RANDOM (4 digits fixed for consistency)
        int random = 1000 + RANDOM.nextInt(9000);

        // 5. FINAL ID
        return year + roleCode + time + random;
    }
}