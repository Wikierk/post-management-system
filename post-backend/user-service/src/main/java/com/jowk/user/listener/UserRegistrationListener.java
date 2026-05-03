package com.jowk.user.listener;

import com.jowk.common.domain.event.UserRegisteredEvent;
import com.jowk.user.core.User;
import com.jowk.user.core.UserRepository;
import com.jowk.user.core.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

    private final UserRepository userRepository;

    @RabbitListener(queues = "user.registration.queue")
    @Transactional
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("Received UserRegisteredEvent for email: {}", event.email());

        if (userRepository.existsById(event.userId())) {
            log.warn("User with ID {} already exists in user-service. Skipping.", event.userId());
            return;
        }

        User userProfile = new User();
        userProfile.setId(event.userId());
        userProfile.setEmail(event.email());
        userProfile.setFirstName(event.firstName());
        userProfile.setLastName(event.lastName());
        userProfile.setStatus(UserStatus.ACTIVE);

        userRepository.save(userProfile);

        log.info("Successfully created user profile for ID: {}", event.userId());
    }
}
