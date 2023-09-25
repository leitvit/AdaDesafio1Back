package com.ada.consumer.controller.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueueInfoResponseDTO {

    private Integer queueApproxSize;

    private List<MessageDTO> allMessagesAttributes;

    @Data
    @NoArgsConstructor
    public static class MessageDTO {
        private String messageId;
        private String receiptHandle;
        private String md5OfBody;
        private String body;
        private Map<String, String> attributes;
        private String md5OfMessageAttributes;
    }

}
