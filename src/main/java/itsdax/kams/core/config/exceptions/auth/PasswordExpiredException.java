package itsdax.kams.core.config.exceptions.auth;

import itsdax.kams.core.config.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class PasswordExpiredException extends BaseException {

    public PasswordExpiredException(String message) {
        super(message, "PASSWORD_EXPIRED", HttpStatus.FORBIDDEN);
    }
}