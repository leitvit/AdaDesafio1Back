package com.ada.consumer.service;

import com.ada.consumer.model.ConsumerFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Service
public class SnsService {

    Logger logger = LoggerFactory.getLogger(SnsService.class);

    final String COMPLIMENTS_TOPIC_NAME = "cielo_compliments";
    final String CRITICS_TOPIC_NAME = "cielo_critics";
    final String SUGGESTIONS_TOPIC_NAME = "cielo_suggestions";

    @Autowired
    SnsClient snsClient;

    @Value("${aws.sns.topic-arn.elogios}")
    String topicArnElogio;
    @Value("${aws.sns.topic-arn.sugestoes}")
    String topicArnSugestoes;
    @Value("${aws.sns.topic-arn.criticas}")
    String topicArnCriticas;
    @Value("${aws.sns.default-group-id}")
    String defaultGroupId;

    HashMap<ConsumerFeedback.FeedbackType, String> snsTopicArnMapping = new HashMap<>();

    @PostConstruct
    public void initClients() {

        //Creates topic if there`s none arn in config yaml
        String complimentsTopicArn = topicArnElogio.isEmpty() ? createSnsTopic(COMPLIMENTS_TOPIC_NAME) : topicArnElogio;
        String criticsTopicArn = topicArnCriticas.isEmpty() ? createSnsTopic(CRITICS_TOPIC_NAME) : topicArnCriticas;
        String suggestionsTopicArn = topicArnSugestoes.isEmpty() ? createSnsTopic(SUGGESTIONS_TOPIC_NAME) : topicArnSugestoes;

        snsTopicArnMapping.put(ConsumerFeedback.FeedbackType.ELOGIO, complimentsTopicArn);
        snsTopicArnMapping.put(ConsumerFeedback.FeedbackType.CRITICA, criticsTopicArn);
        snsTopicArnMapping.put(ConsumerFeedback.FeedbackType.SUGESTAO, suggestionsTopicArn);

    }

    /**
     *
     * @param consumerFeedback
     * @returns publishedMessageId
     * @throws RuntimeException in case of unsuccesfull publishing
     */
    public String publishMessageToSNSTopic(ConsumerFeedback consumerFeedback) throws RuntimeException {
        ConsumerFeedback.FeedbackType feedbackType = consumerFeedback.getFeedbackType();
        String messageBody = consumerFeedback.getMessage();
        String topicArn = snsTopicArnMapping.get(feedbackType);

        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageBody)
                .messageGroupId(defaultGroupId)
                .topicArn(topicArn)
                .messageDeduplicationId(consumerFeedback.getId())
                .build();

        PublishResponse publishResponse = snsClient.publish(publishRequest);

        if (publishResponse.sdkHttpResponse().isSuccessful()) {
            return publishResponse.messageId();
        } else {
            throw new RuntimeException("Publishing message wasn't succesfull." + publishResponse.responseMetadata());
        }
    }

    private String createSnsTopic(String topicName) {
        try {

            Map<String, String> topicAttributes = Map.of(
                    "FifoTopic", "true",
                    "ContentBasedDeduplication", "false");

            CreateTopicRequest topicRequestCompliments = CreateTopicRequest.builder()
                            .name(topicName)
                            .attributes(topicAttributes)
                            .build();

            CreateTopicResponse complimentsTopicResponse = snsClient.createTopic(topicRequestCompliments);

            logger.info("Created " + topicName + " SNS topic.");

            return complimentsTopicResponse.topicArn();
        } catch (SnsException snsEx) {
            logger.error("Couldn't create SNS topic: ", snsEx);
            throw snsEx;
        }
    }

}