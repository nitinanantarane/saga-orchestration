package com.example.CommonService.event;

import lombok.Data;

@Data
public class CancelOrderEvent {
    private String orderId;
    private String orderStatus;
}
