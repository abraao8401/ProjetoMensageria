package com.example.order.model;

import java.util.List;

/**
 * Modelo que representa um pedido (Order) com um ID único,
 * timestamp da criação e uma lista de itens.
 */

public class Order {
    private String id;
    private String timestamp;
    private List<String> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}