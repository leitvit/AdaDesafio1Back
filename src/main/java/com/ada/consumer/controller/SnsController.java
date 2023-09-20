package com.ada.consumer.controller;

import com.ada.consumer.model.ConsumerFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;

@Controller
public class SnsController{

    @Autowired
    SnsClient AwsSnsClient;

    @Value("${aws.sns.topic-arn.elogios}")
    String TopicArnElogio;
    @Value("${aws.sns.topic-arn.sugestoes}")
    String TopicArnSugestoes;
    @Value("${aws.sns.topic-arn.criticas}")
    String TopicArnCriticas;
    @Value("${aws.sns.default-group-id}")
    String defaultGroupId;


    public HashMap<ConsumerFeedback.MessageType, String> snsTopicArnMapping = new HashMap<>();

    public SnsController() {
        this.snsTopicArnMapping.put(ConsumerFeedback.MessageType.ELOGIO, TopicArnElogio);
        this.snsTopicArnMapping.put(ConsumerFeedback.MessageType.CRITICA, TopicArnCriticas);
        this.snsTopicArnMapping.put(ConsumerFeedback.MessageType.SUGESTAO, TopicArnSugestoes);
    }

    public String getTopicArnForMessageType(ConsumerFeedback.MessageType messageType) {
        return snsTopicArnMapping.get(messageType);
    }

    @GetMapping("/sendSnsNotification")
    public ResponseEntity<String> publishMessageToSNSTopic(ConsumerFeedback consumerFeedback) {
        ConsumerFeedback.MessageType feedbackType = consumerFeedback.getFeedbackType();
        String messageBody = consumerFeedback.getMessage();
        String topicArn = getTopicArnForMessageType(feedbackType);

        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageBody)
                .messageGroupId(defaultGroupId)
                .topicArn(topicArn)
                .build();
        AwsSnsClient.publish(publishRequest);
        return new ResponseEntity<>("Mensagem SNS enviada com sucesso.", HttpStatus.OK);
    }

}
