package com.baibei.authservice.controllers;

import com.baibei.authservice.dto.AuthRequest;
import com.baibei.authservice.dto.AuthResponse;
import com.baibei.authservice.dto.RegisterRequest;
import com.baibei.authservice.dto.TokenRefreshRequest;
import com.baibei.authservice.entity.RefreshToken;
import com.baibei.authservice.entity.Role;
import com.baibei.authservice.entity.User;
import com.baibei.authservice.services.RefreshTokenService;
import com.baibei.authservice.services.UserService;
import com.baibei.authservice.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Логин с выдачей Access и Refresh токенов
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            User user = userService.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(authRequest.getUsername()));
            List<String> roles = getRoles(user.getRoles());

            String accessToken = jwtUtil.generateToken(authRequest.getUsername(), roles);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken()));

        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private List<String> getRoles(Set<Role> roleSet) {
        List<String> roles = new ArrayList<>();
        for (Role role : roleSet) {
            roles.add(role.getName());
        }
        return roles;
    }

    /**
     * Регистрация нового пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    /**
     * Обновление Access Token по Refresh Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(token -> {
                    boolean isValid = refreshTokenService.verifyExpiration(token);

                    if (!isValid) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
                    }

                    String username = token.getUsername();
                    String newAccessToken = jwtUtil.generateToken(username, null);

                    return ResponseEntity.ok(new AuthResponse(newAccessToken, requestRefreshToken));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Refresh token not found"));
    }

    /**
     * Logout: удаление Refresh Token
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String username) {
        refreshTokenService.deleteByUsername(username);
        return ResponseEntity.ok("Logged out successfully");
    }
}
