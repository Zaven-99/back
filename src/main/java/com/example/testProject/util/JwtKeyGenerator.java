package com.example.testProject.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class JwtKeyGenerator {

    private JwtKeyGenerator() {
    }

    public static void main(String[] args) {
        int bytes = 32; // 256-bit minimum for HS256
        if (args.length >= 1) {
            try {
                bytes = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
                // keep default
            }
        }

        byte[] key = new byte[bytes];
        new SecureRandom().nextBytes(key);

        // Base64URL is convenient for env vars and properties (no '+' or '/')
        String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(key);
        System.out.println(secret);
    }
}

