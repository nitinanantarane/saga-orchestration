package com.example.ShipmentService.command.api.event;

import com.example.CommonService.event.CancelShipmentEvent;
import com.example.CommonService.event.OrderShippedEvent;
import com.example.ShipmentService.command.api.data.Shipment;
import com.example.ShipmentService.command.api.data.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventHandler {

    private ShipmentRepository shipmentRepository;

    public ShipmentEventHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(orderShippedEvent, shipment);
        shipmentRepository.save(shipment);
    }

    @EventHandler
    public void on(CancelShipmentEvent cancelShipmentEvent) {
        Shipment shipment = shipmentRepository.findById(
                cancelShipmentEvent.getShipmentId()).get();

        shipment.setShipmentStatus(cancelShipmentEvent.getShipmentStatus());
        shipmentRepository.save(shipment);
    }
}
