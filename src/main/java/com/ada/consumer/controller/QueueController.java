package com.ada.consumer.controller;

import com.ada.consumer.model.ConsumerFeedback;
import com.ada.consumer.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> getQueueSize(@RequestParam ConsumerFeedback.FeedbackType feedbackType) {
        try {
            return ResponseEntity.ok(String.valueOf(queueService.getQueueSizeForType(feedbackType)));
        } catch(Exception ex) {
            return ResponseEntity.internalServerError().body("Error communicating with SQS.");
        }
    }

    @GetMapping("v1/queue-info")
    @Operation(
            summary = "Consultar fila SQS.",
            description = "Recupera a última mensagem da fila SQS para cada tipo de feedback."
    )
    public ResponseEntity<String> getQueueInfo() {
        try {
            var information = queueService.getAllInformation();
            System.out.println(information);
            return ResponseEntity.ok(String.valueOf(information));
        } catch(Exception ex) {
            return ResponseEntity.internalServerError().body("Could not retrieve information: Error communicating with SQS.");
        }
    }
}
