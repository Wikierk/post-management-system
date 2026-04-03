package com.jowk.auth.event;

import com.jowk.common.domain.event.UserRegisteredEvent;

public interface UserEventPublisher {
    void publishUserRegisteredEvent(UserRegisteredEvent event);
}
