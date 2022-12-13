package com.example.OrderService.command.api.controller;

import com.example.OrderService.command.api.commands.CreateOrderCommand;
import com.example.OrderService.command.api.model.OrderRestModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    private CommandGateway commandGateway;

    public OrderCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .orderStatus("CREATED")
                .addressId(orderRestModel.getAddressId())
                .productId(orderRestModel.getProductId())
                .userId(orderRestModel.getUserId())
                .quantity(orderRestModel.getQuantity())
                .build();

        commandGateway.sendAndWait(createOrderCommand);
        return "Order Created";
    }
}
