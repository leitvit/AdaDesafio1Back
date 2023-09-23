package com.ada.consumer.service;

import com.ada.consumer.model.ConsumerFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import java.util.*;

import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import javax.annotation.PostConstruct;
import java.util.HashMap;


@Service
public class QueueService {

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

    public Map<String, String> getAllInformation() {
        // here we must poll the three queues and return the parsed content summarized
        List<String> readable_results = new ArrayList<String>();
        List<software.amazon.awssdk.services.sqs.model.Message> messages = new ArrayList<>();
        List<List<software.amazon.awssdk.services.sqs.model.Message>> allinfo = new ArrayList<List<software.amazon.awssdk.services.sqs.model.Message>>();
        List<String> URLs = Arrays.asList(
                "https://sqs.us-east-1.amazonaws.com/908737804402/criticas.fifo",
                "https://sqs.us-east-1.amazonaws.com/908737804402/elogios.fifo",
                "https://sqs.us-east-1.amazonaws.com/908737804402/sugestoes.fifo");
        String readable_aux = "";
        String key = "";
        List<String> keys = new ArrayList<String>();
        keys.add("criticas"); keys.add("elogios"); keys.add("sugestoes");
        Map<String, String> dict = new HashMap<String, String>();

        for(String s: URLs) {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(s)
                    .build();
            messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            allinfo.add(messages);
        }
        for(int i=0; i<3; i++) {
            readable_aux = "";
            for(Message m: allinfo.get(i)){
                key = keys.get(1);
                readable_aux += m.toString() + "\n";
            }
            dict.put(key, readable_aux);
        }
        return dict;
    }
}
