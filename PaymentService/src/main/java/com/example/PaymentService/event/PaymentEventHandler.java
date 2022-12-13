package com.example.PaymentService.event;

import com.example.CommonService.event.CancelPaymentEvent;
import com.example.CommonService.event.PaymentProcessedEvent;
import com.example.PaymentService.data.Payment;
import com.example.PaymentService.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentEventHandler {

    private PaymentRepository paymentRepository;

    public PaymentEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void handle(PaymentProcessedEvent event) {
        Payment payment = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .timestamp(new Date())
                .build();
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(CancelPaymentEvent cancelPaymentEvent) {
        Payment payment = paymentRepository.findById(
                cancelPaymentEvent.getPaymentId()).get();

        payment.setPaymentStatus(cancelPaymentEvent.getPaymentStatus());
        paymentRepository.save(payment);
    }
}
