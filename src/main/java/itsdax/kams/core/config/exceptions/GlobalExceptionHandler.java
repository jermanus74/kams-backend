package itsdax.kams.core.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // 404 NOT FOUND
    // =========================
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNotFound(NoSuchElementException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                buildResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage())
        );
    }

    // =========================
    // 400 BAD REQUEST (custom)
    // =========================
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequest(InvalidRequestException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                buildResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", ex.getMessage())
        );
    }

    // =========================
    // 401 UNAUTHORIZED (FIXED)
    // =========================
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                buildResponse(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", ex.getMessage())
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                buildResponse(HttpStatus.UNAUTHORIZED, "BAD_CREDENTIALS", "Invalid email or password")
        );
    }

    // =========================
    // 415 UNSUPPORTED MEDIA TYPE
    // =========================
    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<?> handleUnsupportedMediaType(UnsupportedMediaTypeException ex) {

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE", ex.getMessage())
        );
    }

    // =========================
    // FILE UPLOAD ERROR
    // =========================
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<?> handleFileUpload(FileUploadException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                buildResponse(HttpStatus.BAD_REQUEST, "FILE_UPLOAD_ERROR", ex.getMessage())
        );
    }

    // =========================
    // BASE EXCEPTION (BEST PRACTICE)
    // =========================
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException ex) {

        return ResponseEntity.status(ex.getStatus()).body(
                buildResponse(ex.getStatus(), ex.getErrorCode(), ex.getMessage())
        );
    }

    // =========================
    // FALLBACK (CATCH-ALL)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                        "Something went wrong. Please contact support.")
        );
    }

    // =========================
    // RESPONSE BUILDER
    // =========================
    private Map<String, Object> buildResponse(HttpStatus status, String error, String message) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);

        return response;
    }
}