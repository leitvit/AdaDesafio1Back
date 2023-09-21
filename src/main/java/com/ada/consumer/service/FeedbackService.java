package com.ada.consumer.service;

import com.ada.consumer.controller.record.ConsumerFeedbackRecord;
import com.ada.consumer.model.ConsumerFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private SnsService snsService;

    public String receiveFeedback(ConsumerFeedbackRecord feedbackRecord) throws RuntimeException {

        ConsumerFeedback consumerFeedback = new ConsumerFeedback(
                UUID.randomUUID().toString(),
                feedbackRecord.message(),
                feedbackRecord.feedbackType(),
                ConsumerFeedback.Status.RECEBIDO
        );

        //saving to a variable for cleaner readability
        var messageId = snsService.publishMessageToSNSTopic(consumerFeedback);

        return messageId;
    }
}
