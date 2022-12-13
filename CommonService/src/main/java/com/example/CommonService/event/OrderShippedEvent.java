package com.example.CommonService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderShippedEvent {
    private String shipmentId;
    private String paymentId;
    private String orderId;
    private String shipmentStatus;
}
