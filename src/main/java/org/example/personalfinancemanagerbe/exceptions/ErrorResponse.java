package org.example.personalfinancemanagerbe.exceptions;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

// Create an error response type - timestamp, status, error, message, path, field errors
public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldViolation> fieldViolationList

) {
    public record FieldViolation(String field, String message){ }

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(),
                message, path, List.of());
    }

    public static ErrorResponse validation(String path, List<FieldViolation> fieldErrors) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(),
                "Validation failed", path, fieldErrors);
    }
}
