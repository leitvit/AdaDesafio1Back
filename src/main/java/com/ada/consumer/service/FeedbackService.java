package com.ada.consumer.service;

import com.ada.consumer.model.ConsumerFeedback;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class FeedbackService {

    public URI postFeedbackOnQueue(ConsumerFeedback consumerFeedback) throws URISyntaxException {
        return new URI("http://uri.location.of.feedback.on.SNS");
    }
}
