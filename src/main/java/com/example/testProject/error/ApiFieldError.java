package com.example.testProject.error;

public record ApiFieldError(
        String field,
        String message
) {
}

