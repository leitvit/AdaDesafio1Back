package com.ada.consumer.controller;

import com.ada.consumer.controller.record.ConsumerFeedbackRecord;
import com.ada.consumer.model.ConsumerFeedback;
import com.ada.consumer.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity postFeedback(@RequestBody @Validated ConsumerFeedbackRecord consumerFeedbackRecord) {
        try {
            var messageId = feedbackService.receiveFeedback(consumerFeedbackRecord);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(messageId);
        } catch (Exception ex) {
            return ResponseEntity
                    .internalServerError()
                    .body("There was an error with the queue sevice. Try again in a few seconds.");
        }
    }



}
