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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserEventPublisher eventPublisher;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtProvider jwtProvider;
    @Mock private GoogleIdTokenVerifier googleIdTokenVerifier;
    @Mock private RabbitMQUserEventPublisher rabbitMQUserEventPublisher;
    @Mock private HttpServletResponse response;

    @InjectMocks
    private AuthService authService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<Cookie> cookieCaptor;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("hashedPassword");
        testUser.setRoles(new HashSet<>());
        testUser.setIdentities(new HashSet<>());
    }


    @Test
    void registerUser_ShouldCreateUserAndPublishEvent_WhenEmailIsUnique() {
        RegisterRequest request = new RegisterRequest("test@example.com", "password123", "Jan", "Kowalski");
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        authService.registerUser(request);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getPasswordHash()).isEqualTo("hashedPassword");
        assertThat(savedUser.getRoles()).hasSize(1);
        assertThat(savedUser.getRoles().iterator().next().getRole()).isEqualTo(Role.CUSTOMER);

        verify(eventPublisher).publishUserRegisteredEvent(any(UserRegisteredEvent.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("test@example.com", "password123", "Jan", "Kowalski");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User with this email already exists");

        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishUserRegisteredEvent(any());
    }


    @Test
    void loginUser_ShouldAuthenticateAndSetCookie_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(testUser));
        when(jwtProvider.generateToken(testUser)).thenReturn("mocked.jwt.token");

        authService.loginUser(request, response);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(response).addCookie(cookieCaptor.capture());

        Cookie cookie = cookieCaptor.getValue();
        assertThat(cookie.getName()).isEqualTo(JwtConstants.JWT_COOKIE_NAME);
        assertThat(cookie.getValue()).isEqualTo("mocked.jwt.token");
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getPath()).isEqualTo("/");
    }

    @Test
    void loginUser_ShouldThrowException_WhenUserNotFoundAfterAuthentication() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.loginUser(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");

        verify(response, never()).addCookie(any());
    }


    @Test
    void loginWithGoogle_ShouldCreateNewUserAndSetCookie_WhenUserDoesNotExist() throws Exception {
        GoogleLoginRequest request = new GoogleLoginRequest("valid_google_token");
        GoogleIdToken mockIdToken = mock(GoogleIdToken.class);
        GoogleIdToken.Payload mockPayload = mock(GoogleIdToken.Payload.class);

        when(googleIdTokenVerifier.verify(request.idToken())).thenReturn(mockIdToken);
        when(mockIdToken.getPayload()).thenReturn(mockPayload);
        when(mockPayload.getEmail()).thenReturn("google@example.com");
        when(mockPayload.get("given_name")).thenReturn("Google");
        when(mockPayload.get("family_name")).thenReturn("User");
        when(mockPayload.getSubject()).thenReturn("google-subject-id");

        when(userRepository.findByEmail("google@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("random-hash");

        User savedGoogleUser = new User();
        savedGoogleUser.setId(UUID.randomUUID());
        savedGoogleUser.setEmail("google@example.com");
        when(userRepository.save(any(User.class))).thenReturn(savedGoogleUser);
        when(jwtProvider.generateToken(savedGoogleUser)).thenReturn("google.jwt.token");

        authService.loginWithGoogle(request, response);

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo("google@example.com");
        assertThat(capturedUser.getIdentities()).hasSize(1);
        assertThat(capturedUser.getIdentities().iterator().next().getProvider()).isEqualTo(IdentityProvider.GOOGLE);
        assertThat(capturedUser.getRoles()).isNotEmpty();

        verify(rabbitMQUserEventPublisher).publishUserRegisteredEvent(any(UserRegisteredEvent.class));

        verify(response).addCookie(cookieCaptor.capture());
        assertThat(cookieCaptor.getValue().getValue()).isEqualTo("google.jwt.token");
    }

    @Test
    void loginWithGoogle_ShouldLinkIdentityAndSetCookie_WhenUserExistsButWithoutGoogleIdentity() throws Exception {
        GoogleLoginRequest request = new GoogleLoginRequest("valid_google_token");
        GoogleIdToken mockIdToken = mock(GoogleIdToken.class);
        GoogleIdToken.Payload mockPayload = mock(GoogleIdToken.Payload.class);

        when(googleIdTokenVerifier.verify(request.idToken())).thenReturn(mockIdToken);
        when(mockIdToken.getPayload()).thenReturn(mockPayload);
        when(mockPayload.getEmail()).thenReturn("test@example.com");
        when(mockPayload.getSubject()).thenReturn("google-subject-id");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtProvider.generateToken(testUser)).thenReturn("google.jwt.token");

        authService.loginWithGoogle(request, response);

        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getIdentities()).hasSize(1);
        assertThat(userCaptor.getValue().getIdentities().iterator().next().getProvider()).isEqualTo(IdentityProvider.GOOGLE);

        verify(rabbitMQUserEventPublisher, never()).publishUserRegisteredEvent(any());

        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void loginWithGoogle_ShouldThrowRuntimeException_WhenTokenIsInvalid() throws Exception {
        GoogleLoginRequest request = new GoogleLoginRequest("invalid_token");
        when(googleIdTokenVerifier.verify(request.idToken())).thenReturn(null);

        assertThatThrownBy(() -> authService.loginWithGoogle(request, response))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to authenticate with Google");

        verify(userRepository, never()).save(any());
    }
}