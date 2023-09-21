package com.ada.consumer.controller;

import com.ada.consumer.model.ConsumerFeedback;
import com.ada.consumer.service.SnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SnsController{

    @Autowired
    private SnsService snsService;

    @PostMapping("sns/")
    public ResponseEntity<String> postFeedback(@RequestBody @Validated ConsumerFeedback feedback) {
        try {
            var response = snsService.publishMessageToSNSTopic(feedback);
            return ResponseEntity.ok().body(response.toString());
        } catch (Exception ex) {

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

}
