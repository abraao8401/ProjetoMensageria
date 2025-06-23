package com.example.notification.listener;
import com.example.inventory.model.InventoryEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {

    @KafkaListener(
            topics = "inventory-events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onInventoryEvent(InventoryEvent event) {
        if (event.isSuccess()) {
            System.out.printf(
                    "[NOTIFICATION] Pedido %s processado com sucesso: %s. Enviando e-mail e SMS de confirmação.%n",
                    event.getOrderId(),
                    event.getMessage()
            );
        } else {
            System.out.printf(
                    "[NOTIFICATION] Pedido %s falhou: %s. Enviando e-mail e SMS de alerta de falha.%n",
                    event.getOrderId(),
                    event.getMessage()
            );
        }
    }
}
