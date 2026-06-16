package com.lucasmanoel.habitos.infrastructure.security;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {
}
