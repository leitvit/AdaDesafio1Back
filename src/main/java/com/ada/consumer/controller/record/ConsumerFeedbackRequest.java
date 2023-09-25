package com.ada.consumer.controller.record;

import com.ada.consumer.model.entity.ConsumerFeedback.FeedbackType;

import java.util.Objects;

public record ConsumerFeedbackRequest(String message, FeedbackType feedbackType) {

    public ConsumerFeedbackRequest {
        Objects.nonNull(message);
        Objects.nonNull(feedbackType);
    }

}
