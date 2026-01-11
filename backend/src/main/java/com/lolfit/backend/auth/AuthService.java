package com.lolfit.backend.auth;

import com.lolfit.backend.user.UserRepository;
import com.lolfit.backend.user.User;
import com.lolfit.backend.auth.dto.RegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder; // Sera configuré plus tard
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }
}
