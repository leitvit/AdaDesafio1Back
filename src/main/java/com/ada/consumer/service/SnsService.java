package com.ada.consumer.service;

import com.ada.consumer.model.ConsumerFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import javax.annotation.PostConstruct;
import java.util.HashMap;


@Service
public class SnsService {

    @Autowired
    SnsClient snsClient;

    @Value("${aws.sns.topic-arn.elogios}")
    String TopicArnElogio;
    @Value("${aws.sns.topic-arn.sugestoes}")
    String TopicArnSugestoes;
    @Value("${aws.sns.topic-arn.criticas}")
    String TopicArnCriticas;
    @Value("${aws.sns.default-group-id}")
    String defaultGroupId;

    HashMap<ConsumerFeedback.FeedbackType, String> snsTopicArnMapping = new HashMap<>();

    @PostConstruct
    public void populateMap() {
        snsTopicArnMapping.put(ConsumerFeedback.FeedbackType.ELOGIO, TopicArnElogio);
        snsTopicArnMapping.put(ConsumerFeedback.FeedbackType.CRITICA, TopicArnCriticas);
        snsTopicArnMapping.put(ConsumerFeedback.FeedbackType.SUGESTAO, TopicArnSugestoes);
    }

    public PublishResponse publishMessageToSNSTopic(ConsumerFeedback consumerFeedback) {
        ConsumerFeedback.FeedbackType feedbackType = consumerFeedback.getFeedbackType();
        String messageBody = consumerFeedback.getMessage();
        String topicArn = snsTopicArnMapping.get(feedbackType);
        System.out.println(topicArn);

        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageBody)
                .messageGroupId(defaultGroupId)
                .topicArn(topicArn)
                .build();

        return snsClient.publish(publishRequest);
    }

}