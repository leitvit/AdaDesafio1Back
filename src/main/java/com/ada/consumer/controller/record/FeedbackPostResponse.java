package com.ada.consumer.controller.record;

import java.util.Objects;

public record FeedbackPostResponse(String messageId) {

    public FeedbackPostResponse {
        Objects.nonNull(messageId);
    }
}
