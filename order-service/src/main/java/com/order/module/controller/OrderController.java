package com.order.module.controller;

import com.order.module.entity.OrderEntity;
import com.order.module.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public OrderEntity create(@RequestBody OrderEntity order) {
        return service.save(order);
    }

    @GetMapping
    public List<OrderEntity> getAll() {
        return service.findAll();
    }
}
