package com.example.OrderService.command.api.aggregate;

import com.example.CommonService.commands.CompleteOrderCommand;
import com.example.CommonService.commands.OrderCancelCommand;
import com.example.CommonService.event.CancelOrderEvent;
import com.example.CommonService.event.OrderCompletedEvent;
import com.example.OrderService.command.api.commands.CreateOrderCommand;
import com.example.OrderService.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.LifecycleHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;

    protected OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        log.info("Order Aggregate started>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getProductId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder()
                .orderStatus(completeOrderCommand.getOrderStatus())
                .orderId(completeOrderCommand.getOrderId())
                .build();

        AggregateLifecycle.apply(orderCompletedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent orderCompletedEvent) {
        this.orderStatus = orderCompletedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(OrderCancelCommand orderCancelCommand) {
        CancelOrderEvent cancelOrderEvent = new CancelOrderEvent();
        BeanUtils.copyProperties(orderCancelCommand, cancelOrderEvent);
        AggregateLifecycle.apply(cancelOrderEvent);
    }

    @EventSourcingHandler
    public void on(CancelOrderEvent cancelOrderEvent) {
        this.orderStatus = cancelOrderEvent.getOrderStatus();
    }
}
