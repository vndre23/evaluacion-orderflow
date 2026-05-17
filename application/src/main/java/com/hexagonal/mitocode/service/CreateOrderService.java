package com.hexagonal.mitocode.service;

import com.hexagonal.mitocode.command.CreateOrderCommand;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.port.in.CreateOrderUseCase;
import com.hexagonal.mitocode.port.out.SaveOrderPort;

/*
 * Servicio que se encarga de la lógica de negocio para crear una orden.
 * Este servicio interactúa con los puertos de entrada y salida para realizar las operaciones necesarias.
 */
public class CreateOrderService implements CreateOrderUseCase {

    private final SaveOrderPort saveOrderPort;

    public CreateOrderService(SaveOrderPort saveOrderPort) {
        this.saveOrderPort = saveOrderPort;
    }

    @Override
    public Order createOrder(CreateOrderCommand command) {
        Order order = Order.create(command.customerId());
        return saveOrderPort.save(order);
    }

}
