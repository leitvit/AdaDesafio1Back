package com.ada.consumer.controller;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("./sns")
public class AWSSNSController {

    @Autowired
    AmazonSNSClient amazonSNSClient;

    @GetMapping("/sendNotification")
    public String publishMessageToSNSTopic() {
        PublishRequest publishRequest = new PublishRequest(
                "aws-arn-here",
                "TESTEEE",
                "Assunto");
        publishRequest.setMessageGroupId("-1");
        amazonSNSClient.publish(publishRequest);
        return "notification send successfully.";
    }

}