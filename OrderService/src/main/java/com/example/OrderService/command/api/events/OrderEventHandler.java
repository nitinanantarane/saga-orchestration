package com.example.OrderService.command.api.events;

import com.example.CommonService.event.CancelOrderEvent;
import com.example.CommonService.event.OrderCompletedEvent;
import com.example.OrderService.command.api.data.Order;
import com.example.OrderService.command.api.data.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        Order order = new Order();
        BeanUtils.copyProperties(orderCreatedEvent, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void handle(OrderCompletedEvent orderCompletedEvent) {
        Order order = orderRepository.findById(orderCompletedEvent.getOrderId()).get();

        order.setOrderStatus(orderCompletedEvent.getOrderStatus());
        orderRepository.save(order);
    }

    @EventHandler
    public void handle(CancelOrderEvent cancelOrderEvent) {
        Order order = orderRepository.findById(cancelOrderEvent.getOrderId()).get();
        order.setOrderStatus(cancelOrderEvent.getOrderStatus());
        orderRepository.save(order);
    }
}
