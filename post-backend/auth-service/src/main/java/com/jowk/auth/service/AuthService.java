package com.jowk.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.jowk.auth.domain.IdentityProvider;
import com.jowk.auth.domain.User;
import com.jowk.auth.domain.UserIdentity;
import com.jowk.auth.domain.UserRole;
import com.jowk.auth.dto.GoogleLoginRequest;
import com.jowk.auth.dto.LoginRequest;
import com.jowk.auth.dto.RegisterRequest;
import com.jowk.auth.event.RabbitMQUserEventPublisher;
import com.jowk.auth.event.UserEventPublisher;
import com.jowk.auth.repository.UserRepository;
import com.jowk.common.domain.event.UserRegisteredEvent;
import com.jowk.common.security.domain.Role;
import com.jowk.common.security.jwt.JwtConstants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final RabbitMQUserEventPublisher rabbitMQUserEventPublisher;

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

    public void loginUser(LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtProvider.generateToken(user);

        Cookie jwtCookie = new Cookie(JwtConstants.JWT_COOKIE_NAME, token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (86400000 / 1000));

        response.addCookie(jwtCookie);
    }

    @Transactional
    public void loginWithGoogle(GoogleLoginRequest request, HttpServletResponse response) {
        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(request.idToken());

            if (idToken == null) {
                throw new IllegalArgumentException("Invalid Google ID token.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String googleSubjectId = payload.getSubject();

            User user = userRepository.findByEmail(email).orElse(null);

            boolean isNewUser = false;

            if (user == null) {
                log.info("New Google user logging in. Registering account for: {}", email);
                isNewUser = true;

                user = new User();
                user.setEmail(email);
                user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString())); // Losowe hasło
                user.setActive(true);

                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(Role.CUSTOMER);
                user.getRoles().add(userRole);
            }

            boolean hasGoogleIdentity = user.getIdentities().stream()
                    .anyMatch(id -> id.getProvider() == IdentityProvider.GOOGLE);

            if (!hasGoogleIdentity) {
                log.info("Linking Google identity to user: {}", email);
                UserIdentity googleIdentity = new UserIdentity();
                googleIdentity.setUser(user);
                googleIdentity.setProvider(IdentityProvider.GOOGLE);
                googleIdentity.setSub(googleSubjectId);

                user.getIdentities().add(googleIdentity);
            }

            user = userRepository.save(user);

            if (isNewUser) {
                rabbitMQUserEventPublisher.publishUserRegisteredEvent(
                        new UserRegisteredEvent(user.getId(), email, firstName, lastName)
                );
            }

            String jwt = jwtProvider.generateToken(user);

            Cookie jwtCookie = new Cookie(JwtConstants.JWT_COOKIE_NAME, jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // Na produkcji zmień na true dla HTTPS
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400); // 24 godziny

            response.addCookie(jwtCookie);
            log.info("Successfully authenticated user via Google: {}", email);

        } catch (Exception e) {
            log.error("Google authentication failed: {}", e.getMessage());
            throw new RuntimeException("Failed to authenticate with Google", e);
        }
    }
}
