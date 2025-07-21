package com.trifork.fhir_kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MessageConsumerService {
//
//    @KafkaListener(topics = "${kafka.topics.fhir-resource-created}")
//    public void listen(ConsumerRecord<String, byte[]> record, Acknowledgment acknowledgment) {
//        try {
//            String message = new String(record.value(), StandardCharsets.UTF_8);
//            String key = record.key();
//
//            System.out.println("Received message with key '" + key + "': " + message);
//
//            // Process the message here
//            processMessage(message);
//
//            // Acknowledge the message
//            acknowledgment.acknowledge();
//        } catch (Exception e) {
//            System.err.println("Error processing message: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void processMessage(String message) {
//        // Add your message processing logic here
//        System.out.println("Processing: " + message);
//    }
}