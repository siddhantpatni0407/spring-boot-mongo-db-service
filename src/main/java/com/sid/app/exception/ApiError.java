package com.sid.app.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiError(
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ApiError of(HttpStatus status, String message, String path) {
        return new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path);
    }
}
