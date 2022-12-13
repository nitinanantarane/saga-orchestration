package com.example.ShipmentService.command.api.aggregate;

import com.example.CommonService.commands.ShipmentCancelCommand;
import com.example.CommonService.event.CancelShipmentEvent;
import com.example.CommonService.event.OrderShippedEvent;
import com.example.CommonService.commands.ShipOrderCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class ShipmentAggregate {
    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String paymentId;
    private String shipmentStatus;

    public ShipmentAggregate() {

    }

    @CommandHandler
    public ShipmentAggregate(ShipOrderCommand command) {
        OrderShippedEvent orderShippedEvent =
                OrderShippedEvent.builder()
                        .shipmentId(command.getShipmentId())
                        .paymentId(command.getPaymentId())
                        .orderId(command.getOrderId())
                        .shipmentStatus("COMPLETED")
                        .build();

        AggregateLifecycle.apply(orderShippedEvent);
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        this.shipmentId = orderShippedEvent.getShipmentId();
        this.shipmentStatus = orderShippedEvent.getShipmentStatus();
        this.orderId = orderShippedEvent.getOrderId();
        this.paymentId = orderShippedEvent.getPaymentId();
    }

    @CommandHandler
    public void handle(ShipmentCancelCommand shipmentCancelCommand) {
        CancelShipmentEvent cancelShipmentEvent = new CancelShipmentEvent();
        BeanUtils.copyProperties(shipmentCancelCommand, cancelShipmentEvent);
        AggregateLifecycle.apply(cancelShipmentEvent);
    }

    @EventSourcingHandler
    public void on(CancelShipmentEvent cancelShipmentEvent) {
        this.shipmentStatus = cancelShipmentEvent.getShipmentStatus();
    }
}
