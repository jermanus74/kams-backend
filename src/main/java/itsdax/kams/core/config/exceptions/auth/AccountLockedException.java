package itsdax.kams.core.config.exceptions.auth;

import itsdax.kams.core.config.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class AccountLockedException extends BaseException {

    public AccountLockedException(String message) {
        super(message, "ACCOUNT_LOCKED", HttpStatus.FORBIDDEN);
    }
}