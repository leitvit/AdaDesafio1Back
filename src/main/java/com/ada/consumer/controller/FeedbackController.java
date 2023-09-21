package com.ada.consumer.controller;

import com.ada.consumer.model.ConsumerFeedback;
import com.ada.consumer.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("v1/feedback")
    public ResponseEntity postFeedback(@RequestBody @Validated ConsumerFeedback consumerFeedback) {
        try {
            return ResponseEntity
                    .created(feedbackService.postFeedbackOnQueue(consumerFeedback))
                    .build();
        } catch (Exception ex) {
            return ResponseEntity
                    .internalServerError()
                    .body("There was an error with the queue service. Try again in a few seconds.");
        }
    }
}
