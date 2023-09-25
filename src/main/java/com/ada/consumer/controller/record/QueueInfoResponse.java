package com.ada.consumer.controller.record;

import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

public record QueueInfoResponse(Integer queueAproxSize, List<Message> messageAttributes) {

    public QueueInfoResponse(Integer queueAproxSize) {
        this(queueAproxSize, null);
    }

    public QueueInfoResponse(List<Message> messageAttributes) {
        this(null, messageAttributes);
    }
}
