package itsdax.kams.core.config.exceptions;

import org.springframework.http.HttpStatus;

public class FileUploadException extends BaseException {

    public FileUploadException(String message) {
        super(message, "FILE_UPLOAD_ERROR", HttpStatus.BAD_REQUEST);
    }
}