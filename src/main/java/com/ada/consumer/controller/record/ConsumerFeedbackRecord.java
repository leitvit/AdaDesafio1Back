package com.ada.consumer.controller.record;

import com.ada.consumer.model.ConsumerFeedback.FeedbackType;

import java.util.Objects;

public record ConsumerFeedbackRecord(String message, FeedbackType feedbackType) {

    public ConsumerFeedbackRecord {
        Objects.nonNull(message);
        Objects.nonNull(feedbackType);
    }

}
