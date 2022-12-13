package com.example.CommonService.event;

import lombok.Data;

@Data
public class CancelShipmentEvent {
    private String orderId;
    private String paymentId;
    private String shipmentId;
    private String shipmentStatus;
}
