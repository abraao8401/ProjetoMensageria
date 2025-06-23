package com.example.inventory.controller;

import com.example.inventory.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("orders", inventoryService.getProcessedOrders());
        model.addAttribute("stock", inventoryService.getStock());
        model.addAttribute("events", inventoryService.getEvents());
        return "inventory";
    }
}
