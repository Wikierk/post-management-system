package com.jowk.auth.service;

import com.jowk.auth.domain.User;
import com.jowk.auth.domain.UserRole;
import com.jowk.auth.dto.RegisterRequest;
import com.jowk.auth.event.UserEventPublisher;
import com.jowk.auth.repository.UserRepository;
import com.jowk.common.domain.event.UserRegisteredEvent;
import com.jowk.common.security.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher eventPublisher;

    @Transactional
    public void registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(Role.CUSTOMER);

        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getId(),
                request.email(),
                request.firstName(),
                request.lastName()
        );
        try {
            eventPublisher.publishUserRegisteredEvent(event);
        } catch (Exception ex) {
            log.error("Failed to publish UserRegisteredEvent for user {}: {}", 
                    savedUser.getId(), ex.getMessage(), ex);
        }
    }
}
