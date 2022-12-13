package com.example.PaymentService.aggregate;

import com.example.CommonService.commands.PaymentCancelCommand;
import com.example.CommonService.commands.ValidatePaymentCommand;
import com.example.CommonService.event.CancelPaymentEvent;
import com.example.CommonService.event.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;

    public PaymentAggregate() {

    }

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(
            validatePaymentCommand.getPaymentId(), validatePaymentCommand.getOrderId());
        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }

    @CommandHandler
    public void handle(PaymentCancelCommand paymentCancelCommand) {
        CancelPaymentEvent cancelPaymentEvent = new CancelPaymentEvent();
        BeanUtils.copyProperties(paymentCancelCommand, cancelPaymentEvent);
        log.error("Apply payment cancel event");
        AggregateLifecycle.apply(cancelPaymentEvent);
    }

    @EventSourcingHandler
    public void on(CancelPaymentEvent cancelPaymentEvent) {
        this.paymentStatus = cancelPaymentEvent.getPaymentStatus();
    }
}
