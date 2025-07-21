package com.trifork.fhir_kafka.service;

import com.trifork.fhir_kafka.factory.KafkaConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MessageProducerService {
    private   KafkaConnectionFactory kafkaConnectionFactory;
    private int messageCounter = 1;
    @Autowired
    public MessageProducerService(KafkaConnectionFactory kafkaConnectionFactory) {
        this.kafkaConnectionFactory = kafkaConnectionFactory;
    }

    @Scheduled(fixedRate = 2000000) // 60000ms = 1 minute
    public void sendTestMessage() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String testMessage = String.format("Test message #%d sent at %s", messageCounter++, timestamp);

        byte[] messageBytes = testMessage.getBytes(StandardCharsets.UTF_8);

        kafkaConnectionFactory.sendMessage(
                kafkaConnectionFactory.getFhirResourceCreatedTopic(),
                "test-key-" + messageCounter,
                messageBytes
        );

        System.out.println("Sent message: " + testMessage);
    }

    public void sendCustomMessage(String message) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        kafkaConnectionFactory.sendMessage(
                kafkaConnectionFactory.getFhirResourceCreatedTopic(),
                messageBytes
        );
        System.out.println("Sent custom message: " + message);
    }
}