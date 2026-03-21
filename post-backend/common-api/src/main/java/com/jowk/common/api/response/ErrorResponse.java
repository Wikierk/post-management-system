package com.jowk.common.api.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        Integer status,
        ErrorCode errorCode,
        String message,
        LocalDateTime timestamp
) { }
