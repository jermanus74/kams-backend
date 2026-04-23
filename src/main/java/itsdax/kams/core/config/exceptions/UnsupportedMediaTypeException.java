package itsdax.kams.core.config.exceptions;

import org.springframework.http.HttpStatus;

public class UnsupportedMediaTypeException extends BaseException {

    public UnsupportedMediaTypeException(String message) {
        super(message, "UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}