package com.ada.consumer.controller;

import com.ada.consumer.controller.record.ConsumerFeedbackRecord;
import com.ada.consumer.controller.record.FeedbackPostResponse;
import com.ada.consumer.controller.record.GenericErrorResponse;
import com.ada.consumer.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "v1", produces = "text/plain")
@Tag(name = "bootcampback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping(
            value = "feedback",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Envia feedback para tópico SNS.",
            description = "Envia a mensagem para o tópico referente ao tipo de feedback." +
                    " As filas SQS estão configuradas como subscribers destes tópicos." +
                    " Retorna um ID único referente a mensagem publicada."
    )
    public ResponseEntity postFeedback(@RequestBody @Validated ConsumerFeedbackRecord consumerFeedbackRecord) {
        try {
            var messageId = feedbackService.receiveFeedback(consumerFeedbackRecord);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new FeedbackPostResponse(messageId));
        } catch (Exception ex) {
            return ResponseEntity
                    .internalServerError()
                    .body(
                            new GenericErrorResponse(
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "There was an error with the queue service. Try again in a few seconds."
                            )
                    );
        }
    }



}
