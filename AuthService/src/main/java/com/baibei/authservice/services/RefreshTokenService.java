package com.baibei.authservice.services;

import com.baibei.authservice.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);

    Optional<RefreshToken> findByToken(String token);

    boolean verifyExpiration(RefreshToken token);

    void deleteByUsername(String username);
}
