package com.example.demo.auth;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String username
) {
}
