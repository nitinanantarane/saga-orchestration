package com.example.CommonService.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class ShipmentCancelCommand {
    @TargetAggregateIdentifier
    private String shipmentId;
    private String paymentId;
    private String orderId;
    private String shipmentStatus = "CANCELLED";
}
