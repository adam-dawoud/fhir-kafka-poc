package com.trifork.fhir_kafka.factory;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaConnectionFactory {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topics.fhir-resource-created}")
    private String fhirResourceCreatedTopic;

    public KafkaConnectionFactory(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void init() {
        createTopicIfNotExists();
    }

    public void sendMessage(String topic, String key, byte[] message) {
        kafkaTemplate.send(topic, key, message);
    }

    public void sendMessage(String topic, byte[] message) {
        kafkaTemplate.send(topic, message);
    }

    public String getFhirResourceCreatedTopic() {
        return fhirResourceCreatedTopic;
    }

    private void createTopicIfNotExists() {
        try {
            Properties props = new Properties();
            props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

            try (AdminClient adminClient = AdminClient.create(props)) {
                if (!adminClient.listTopics().names().get().contains(fhirResourceCreatedTopic)) {
                    NewTopic newTopic = new NewTopic(fhirResourceCreatedTopic, 1, (short) 1);
                    adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
                    System.out.println("Topic '" + fhirResourceCreatedTopic + "' created successfully");
                } else {
                    System.out.println("Topic '" + fhirResourceCreatedTopic + "' already exists");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error creating topic: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Unexpected error during topic creation: " + e.getMessage());
        }
    }
}
