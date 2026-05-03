package com.jowk.auth.event;

import com.jowk.auth.config.RabbitMQConfig;
import com.jowk.common.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQUserEventPublisher implements UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
    }
}

