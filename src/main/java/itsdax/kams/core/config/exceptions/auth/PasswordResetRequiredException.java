package itsdax.kams.core.config.exceptions.auth;

import itsdax.kams.core.config.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class PasswordResetRequiredException extends BaseException {

    public PasswordResetRequiredException(String message) {
        super(message, "PASSWORD_RESET_REQUIRED", HttpStatus.FORBIDDEN);
    }
}