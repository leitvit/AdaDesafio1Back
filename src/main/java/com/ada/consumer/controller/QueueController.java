package com.ada.consumer.controller;

import com.ada.consumer.controller.record.GenericErrorResponse;
import com.ada.consumer.controller.record.QueueInfoResponse;
import com.ada.consumer.model.entity.ConsumerFeedback;
import com.ada.consumer.service.QueueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(produces = "application/json")
@Tag(name = "bootcampback")
public class QueueController {

    @Autowired
    private QueueService queueService;

    @GetMapping(value = "v1/queue-size", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Verificar tamanho da fila.",
            description = "Retorna o número aproximado de mensagens disponíveis na fila SQS requisitada."
    )
    public ResponseEntity getQueueSize(@RequestParam ConsumerFeedback.FeedbackType feedbackType) {
        try {
            return ResponseEntity
                    .ok(
                            new QueueInfoResponse(
                                    queueService.getQueueSizeForType(feedbackType)
                            )
                    );
        } catch(Exception ex) {
            return ResponseEntity
                    .internalServerError()
                    .body(
                            new GenericErrorResponse(
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Error communicating with SQS."
                            )
                    );
        }
    }

    @GetMapping(value = "v1/queue-info/by", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Consultar fila SQS.",
            description = "Recupera a última mensagem da fila SQS para cada tipo de feedback."
    )
    public ResponseEntity getQueueInfo(@RequestParam ConsumerFeedback.FeedbackType type) {
        try {
            var information = queueService.getAllInformationFromQueueByType(type);
            ObjectMapper mapper = new ObjectMapper();
            var responseBody = mapper.createObjectNode();
            responseBody.put("messagesAttributtes", information.toString());
            return ResponseEntity.ok(responseBody);
        } catch(Exception ex) {
            return ResponseEntity
                    .internalServerError()
                    .body(
                            new GenericErrorResponse(
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Could not retrieve information: Error communicating with SQS."
                            )
                    );
        }
    }
}
