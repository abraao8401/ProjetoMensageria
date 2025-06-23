package com.example.inventory.service;
import com.example.inventory.model.InventoryEvent;
import com.example.order.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;
    private final Map<String, Integer> stock = new ConcurrentHashMap<>();
    private final List<Order> processedOrders = new ArrayList<>();
    private final List<InventoryEvent> events = new ArrayList<>();

    public InventoryService(KafkaTemplate<String, InventoryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        // Estoque inicial com produtos eletrônicos
        stock.put("Smartphone", 10);
        stock.put("Tablet", 5);
        stock.put("Notebook", 4);
        stock.put("Fone de Ouvido", 15);
        stock.put("Mouse Gamer", 8);
        stock.put("Teclado Mecânico", 6);
        stock.put("Monitor", 3);
        stock.put("Caixa de Som Bluetooth", 7);
        stock.put("Carregador Portátil", 12);
    }


    public List<InventoryEvent> getEvents() {
        return events;
    }
    public Map<String, Integer> getStock() {
        return stock;
    }

    public List<Order> getProcessedOrders() {
        return processedOrders;
    }

    @KafkaListener(
            topics = "orders",
            groupId = "inventory-group",
            containerFactory = "orderKafkaListenerFactory"
    )
    public void consumeOrder(Order order) {
        boolean success = true;
        StringBuilder resultMsg = new StringBuilder();

        // 1) Conta quantas unidades de cada item o pedido solicita
        Map<String, Long> orderCounts = order.getItems().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        synchronized (this) {
            // 2) Verifica disponibilidade para cada item
            for (Map.Entry<String, Long> entry : orderCounts.entrySet()) {
                String item = entry.getKey();
                long requested = entry.getValue();
                int available = stock.getOrDefault(item, 0);

                if (available < requested) {
                    success = false;
                    resultMsg.append("Estoque insuficiente para ")
                            .append(item)
                            .append(" (solicitado: ")
                            .append(requested)
                            .append(", disponível: ")
                            .append(available)
                            .append("). ");
                }
            }

            // 3) Se possível, aplica a reserva no estoque
            if (success) {
                for (Map.Entry<String, Long> entry : orderCounts.entrySet()) {
                    String item = entry.getKey();
                    long requested = entry.getValue();
                    stock.put(item, stock.get(item) - (int) requested);
                }
                resultMsg.append("Estoque reservado com sucesso.");
            }
        }

        // Armazena e publica o evento
        processedOrders.add(order);
        InventoryEvent event = new InventoryEvent(order.getId(), success, resultMsg.toString());
        events.add(event);
        kafkaTemplate.send("inventory-events", event);
    }
}
