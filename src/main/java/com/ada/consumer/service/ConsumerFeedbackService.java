package com.ada.consumer.service;

import com.ada.consumer.controller.record.ConsumerFeedbackRequest;
import com.ada.consumer.model.entity.ConsumerFeedback;
import com.ada.consumer.model.repository.ConsumerFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConsumerFeedbackService {

    @Autowired
    private ConsumerFeedbackRepository consFeedbackRepository;

    @Autowired
    private SnsService snsService;

    public String publishFeedback(ConsumerFeedbackRequest rawFeedback) throws RuntimeException {
        //saving to a variable for cleaner readability
        var messageId = snsService.publishMessageToSNSTopic(rawFeedback);

        return messageId;
    }

    public ConsumerFeedback saveFeedbacks(ConsumerFeedbackRequest rawFeedback) {

        ConsumerFeedback consumerFeedback = new ConsumerFeedback(
                UUID.randomUUID(),
                rawFeedback.message(),
                rawFeedback.feedbackType(),
                ConsumerFeedback.Status.RECEBIDO
        );

        return consFeedbackRepository.save(consumerFeedback);
    }
}
