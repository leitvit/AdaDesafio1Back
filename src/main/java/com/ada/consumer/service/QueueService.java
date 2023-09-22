package com.ada.consumer.service;

import com.ada.consumer.model.ConsumerFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QueueService {

    @Autowired
    private SqsClient sqsClient;

    public Long getQueueSizeForType(ConsumerFeedback.FeedbackType feedbackType) {
        sqsClient.listQueues();//TODO implement logic
        return 1L;
    }

    public List<List<Message>> getAllInformation() {
        // here we must poll the three queues and return the parsed content summarized
        List<software.amazon.awssdk.services.sqs.model.Message> messages = new ArrayList<>();
        List<List<software.amazon.awssdk.services.sqs.model.Message>> allinfo = new ArrayList<List<software.amazon.awssdk.services.sqs.model.Message>>();
        List<String> URLs = Arrays.asList("https://sqs.us-east-1.amazonaws.com/908737804402/criticas.fifo", "https://sqs.us-east-1.amazonaws.com/908737804402/elogios.fifo", "https://sqs.us-east-1.amazonaws.com/908737804402/sugestoes.fifo");
        for(String s: URLs) {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(s)
                    .build();
            messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            allinfo.add(messages);
            System.out.println(messages);
            System.out.println("\n");
        }
        return allinfo;
    }
}
