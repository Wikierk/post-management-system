package com.jowk.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "user.exchange";
    public static final String QUEUE_NAME = "user.registration.queue";
    public static final String ROUTING_KEY = "user.registered.routing.key";

    @Bean
    public Queue registrationQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue registrationQueue, DirectExchange userExchange) {
        return BindingBuilder.bind(registrationQueue).to(userExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
