package itsdax.kams.core.config.exceptions;


import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException {

    public DuplicateResourceException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.CONFLICT);
    }
}