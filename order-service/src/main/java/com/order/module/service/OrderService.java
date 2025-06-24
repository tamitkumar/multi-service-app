package com.order.module.service;

import com.order.module.entity.OrderEntity;
import com.order.module.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderEntity save(OrderEntity order) {
        if(Objects.isNull(order)) {
           return null;
        }
        final OrderEntity[] result = new OrderEntity[1];
        if(Objects.nonNull(order.getId())) {
            orderRepository.findById(order.getId()).ifPresentOrElse(existingOrder -> {
                System.out.println("Order already exists: " + existingOrder.getId());
                existingOrder.setItem(order.getItem());
                existingOrder.setQuantity(order.getQuantity());
                result[0] = orderRepository.save(existingOrder);
            }, () -> {
                System.out.println("Creating new order with ID: " + order.getId());
                result[0] = orderRepository.save(order);
            });
        } else {
            System.out.println("Order not found: " + order.getId());
            result[0] = orderRepository.save(order);
        }

        return result[0];
    }

    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }
}
