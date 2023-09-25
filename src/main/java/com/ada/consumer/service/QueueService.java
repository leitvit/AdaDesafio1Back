package com.ada.consumer.service;

import com.ada.consumer.controller.record.ConsumerFeedbackRequest;
import com.ada.consumer.model.entity.ConsumerFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

import software.amazon.awssdk.services.sqs.model.*;

import java.util.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;


@Service
public class QueueService {

    @Autowired
    private ConsumerFeedbackService consFeedbackService;

    @Autowired
    private SqsClient sqsClient;

    @Value("${aws.sqs.queue-url.elogios}")
    String queueUrlElogio;
    @Value("${aws.sqs.queue-url.sugestoes}")
    String queueUrlSugestoes;
    @Value("${aws.sqs.queue-url.criticas}")
    String queueUrlCriticas;

    HashMap<ConsumerFeedback.FeedbackType, String> sqsQueueURLMapping = new HashMap<>();
    @PostConstruct
    public void initQueueURL() {
        sqsQueueURLMapping.put(ConsumerFeedback.FeedbackType.ELOGIO, queueUrlElogio);
        sqsQueueURLMapping.put(ConsumerFeedback.FeedbackType.SUGESTAO, queueUrlSugestoes);
        sqsQueueURLMapping.put(ConsumerFeedback.FeedbackType.CRITICA, queueUrlCriticas);
    }

    public Integer getQueueSizeForType(ConsumerFeedback.FeedbackType feedbackType) {
        String queueUrl = sqsQueueURLMapping.get(feedbackType);

        GetQueueAttributesRequest attributesRequest = GetQueueAttributesRequest.builder()
                .queueUrl(queueUrl)
                .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES)
                .build();

        GetQueueAttributesResponse attributesResponse = sqsClient.getQueueAttributes(attributesRequest);
        return Integer.parseInt(attributesResponse.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));
    }

    public List<Message> getAllInformationFromQueueByType(ConsumerFeedback.FeedbackType feedbackType) {
        // here we must poll the three queues and return the parsed content summarized
        Integer maxMessages = 5;
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsQueueURLMapping.get(feedbackType))
                .maxNumberOfMessages(5)
                .build();
        List<Message> messagesRequested;
        List<Message> allMessages = new ArrayList<>();

        do {
            messagesRequested = sqsClient.receiveMessage(receiveMessageRequest).messages();

            var batchDeleteReq = DeleteMessageBatchRequest
                    .builder()
                    .queueUrl(sqsQueueURLMapping.get(feedbackType));

            List<DeleteMessageBatchRequestEntry> deleteEntries = new ArrayList<>();

            for (Message message : messagesRequested) {
                allMessages.add(message);
                consFeedbackService.saveFeedbacks(
                        new ConsumerFeedbackRequest(
                                message.body(),
                                feedbackType
                        )
                );
                deleteEntries.add(DeleteMessageBatchRequestEntry
                        .builder()
                        .id(message.messageId())
                        .receiptHandle(message.receiptHandle())
                        .build()
                );
            }
            sqsClient.deleteMessageBatch(batchDeleteReq.entries(deleteEntries).build());
        } while (maxMessages == messagesRequested.size());

        return allMessages;
    }
}
