package com.example.CommonService.event;

import lombok.Data;

@Data
public class CancelPaymentEvent {
    private String orderId;
    private String paymentId;
    private String paymentStatus;
}
