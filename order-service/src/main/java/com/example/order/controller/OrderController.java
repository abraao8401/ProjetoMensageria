package com.example.order.controller;
import com.example.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Controlador REST responsável por expor a API POST /orders.
 * Cada pedido é enviado para o tópico Kafka "orders".
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    private static final String TOPIC = "orders";

    @PostMapping
    public String sendOrder(@RequestBody Order order) {
        // Gera um UUID e timestamp para o pedido
        order.setId(UUID.randomUUID().toString());
        order.setTimestamp(Instant.now().toString());

        // Envia o pedido para o tópico Kafka
        kafkaTemplate.send(TOPIC, order);

        return "✅ Pedido enviado para o Kafka com sucesso!";
    }
}