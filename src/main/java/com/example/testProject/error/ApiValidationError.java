package com.example.testProject.error;

import java.time.Instant;
import java.util.List;

public record ApiValidationError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<ApiFieldError> fields
) {
}

