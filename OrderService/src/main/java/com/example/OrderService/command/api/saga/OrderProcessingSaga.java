package com.example.OrderService.command.api.saga;

import com.example.CommonService.commands.*;
import com.example.CommonService.event.*;
import com.example.CommonService.model.User;
import com.example.CommonService.queries.GetUserPaymentDetailsQuery;
import com.example.OrderService.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    public OrderProcessingSaga() {
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void on(OrderCreatedEvent orderCreatedEvent) {
        log.info("OrderCreatedEvent in saga for order id {} ", orderCreatedEvent.getOrderId());

        try {
            // rollback transaction
            // if(true) throw new Exception("Order Error!!!");

            GetUserPaymentDetailsQuery getUserPaymentDetailsQuery =
                    new GetUserPaymentDetailsQuery(orderCreatedEvent.getUserId());
            User user = queryGateway.query(getUserPaymentDetailsQuery,
                    ResponseTypes.instanceOf(User.class)).join();

            ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                    .paymentId(UUID.randomUUID().toString())
                    .orderId(orderCreatedEvent.getOrderId())
                    .cardDetails(user.getCardDetails())
                    .build();

            commandGateway.sendAndWait(validatePaymentCommand);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            cancelOrder(orderCreatedEvent.getOrderId());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void on(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("PaymentProcessedEvent in saga for order id {} payment id {}",
                paymentProcessedEvent.getOrderId(), paymentProcessedEvent.getPaymentId());

        try {
            // mimic payment cancelled
            //if (true) throw new Exception("Payment Error");

            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .paymentId(paymentProcessedEvent.getPaymentId())
                    .orderId(paymentProcessedEvent.getOrderId())
                    .build();

            commandGateway.sendAndWait(shipOrderCommand);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            cancelPayment(paymentProcessedEvent);
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void on(OrderShippedEvent orderShippedEvent) {
        log.info("OrderShippedEvent in saga for order id {}", orderShippedEvent.getOrderId());

        try {
            // mimic shipment cancelled
            if (true) throw new Exception("Shipment Error!!!");

            CompleteOrderCommand completeOrderCommand = CompleteOrderCommand
                    .builder()
                    .orderId(orderShippedEvent.getOrderId())
                    .orderStatus("Approved")
                    .build();

            commandGateway.sendAndWait(completeOrderCommand);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            cancelShipment(orderShippedEvent);
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void on(OrderCompletedEvent orderCompletedEvent) {
        log.info("OrderCompletedEvent in saga for order id {}", orderCompletedEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(CancelPaymentEvent cancelPaymentEvent) {
        log.info("Payment cancelled event {}", cancelPaymentEvent.getOrderId());
        cancelOrder(cancelPaymentEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void on(CancelShipmentEvent cancelShipmentEvent) {
        log.info("Shipment cancelled event {}", cancelShipmentEvent.getOrderId());
        PaymentCancelCommand paymentCancelCommand = new PaymentCancelCommand(
                cancelShipmentEvent.getPaymentId(), cancelShipmentEvent.getOrderId()
        );
        commandGateway.send(paymentCancelCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void on(CancelOrderEvent cancelOrderEvent) {
        log.info("Order cancelled event {}", cancelOrderEvent.getOrderId());
    }

    private void cancelOrder(String orderId) {
        OrderCancelCommand orderCancelCommand = new OrderCancelCommand(orderId);
        commandGateway.send(orderCancelCommand);
    }

    private void cancelPayment(PaymentProcessedEvent paymentProcessedEvent) {
        PaymentCancelCommand paymentCancelCommand = new PaymentCancelCommand(
                paymentProcessedEvent.getPaymentId(), paymentProcessedEvent.getOrderId()
        );
        commandGateway.send(paymentCancelCommand);
    }

    private void cancelShipment(OrderShippedEvent orderShippedEvent) {
        ShipmentCancelCommand shipmentCancelCommand = new ShipmentCancelCommand(
                orderShippedEvent.getShipmentId(), orderShippedEvent.getPaymentId(),
                orderShippedEvent.getOrderId()
        );
        commandGateway.send(shipmentCancelCommand);
    }
}
