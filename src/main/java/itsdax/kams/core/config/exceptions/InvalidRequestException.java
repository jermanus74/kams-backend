package itsdax.kams.core.config.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BaseException {

    public InvalidRequestException(String message) {
        super(message, "INVALID_REQUEST", HttpStatus.BAD_REQUEST);
    }
}