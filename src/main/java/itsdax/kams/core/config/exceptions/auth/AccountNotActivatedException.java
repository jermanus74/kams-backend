package itsdax.kams.core.config.exceptions.auth;

import itsdax.kams.core.config.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class AccountNotActivatedException extends BaseException {

    public AccountNotActivatedException(String message) {
        super(message, "ACCOUNT_NOT_ACTIVATED", HttpStatus.FORBIDDEN);
    }
}