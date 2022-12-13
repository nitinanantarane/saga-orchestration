package com.example.CommonService.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class OrderCancelCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String orderStatus = "CANCELLED";

}
