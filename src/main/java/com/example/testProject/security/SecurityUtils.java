package com.example.testProject.security;

import com.example.testProject.entity.User;
import com.example.testProject.error.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Authentication required");
        }
        if (auth.getPrincipal() instanceof CustomUserDetails cud) {
            return cud.getUser();
        }
        throw new UnauthorizedException("Authentication required");
    }
}

