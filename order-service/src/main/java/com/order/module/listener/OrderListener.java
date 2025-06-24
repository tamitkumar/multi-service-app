package com.order.module.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    @KafkaListener(topics = "user-topic", groupId = "order-group")
    public void listen(String message) {
        System.out.println("📥 OrderService received message from Kafka: " + message);
        // You can process the message or call OrderService here
    }
}
