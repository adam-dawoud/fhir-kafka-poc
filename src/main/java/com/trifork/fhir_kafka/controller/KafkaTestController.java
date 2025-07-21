package com.trifork.fhir_kafka.controller;

import com.trifork.fhir_kafka.service.MessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fhir")
@CrossOrigin(origins = "*", maxAge = 3600)
public class KafkaTestController {

    private  MessageProducerService messageProducerService;
    @Autowired
    public KafkaTestController(MessageProducerService messageProducerService) {
        this.messageProducerService = messageProducerService;
    }

    @GetMapping("/health")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "fhir-kafka-app");
        response.put("port", 9595);
        response.put("kafka_enabled", true);
        response.put("timestamp", System.currentTimeMillis());
        response.put("cors_enabled", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody String message) {
        try {
            messageProducerService.sendCustomMessage(message);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to Kafka topic successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send message: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/status")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "FHIR Kafka Integration");
        response.put("version", "1.0.0");
        response.put("kafka_topic", "fhir-resource-created");
        response.put("scheduler_enabled", true);
        response.put("cors_enabled", true);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptionsRequest() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")
                .header("Access-Control-Max-Age", "3600")
                .build();
    }
}
