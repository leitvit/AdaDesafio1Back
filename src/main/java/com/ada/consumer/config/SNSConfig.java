package com.ada.consumer.config;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SNSConfig {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Primary
    @Bean
    public AmazonSNSClient getAWSSNSClient() {
        return (AmazonSNSClient) AmazonSNSClientBuilder.standard()
                .withRegion(region)
                .build();
    }
}
