package com.ada.consumer.controller;

import com.ada.consumer.model.ConsumerFeedback;
import com.ada.consumer.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QueueController {

    @Autowired
    private QueueService queueService;

    @GetMapping("v1/queue-size")
    public ResponseEntity getQueueSize(@RequestParam ConsumerFeedback.FeedbackType feedbackType) {
        try {
            return ResponseEntity.ok(queueService.getQueueSizeForType(feedbackType));
        } catch(Exception ex) {
            return ResponseEntity.internalServerError().body("Error communicating with SQS.");
        }
    }
}
