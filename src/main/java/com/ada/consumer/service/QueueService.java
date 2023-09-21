package com.ada.consumer.service;

import com.ada.consumer.model.ConsumerFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

@Service
public class QueueService {

    @Autowired
    private SqsClient sqsClient;

    public Long getQueueSizeForType(ConsumerFeedback.FeedbackType feedbackType) {
        sqsClient.listQueues();//TODO implement logic
        return 1L;
    }
}
