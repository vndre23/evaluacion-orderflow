package com.hexagonal.mitocode.service;

import com.hexagonal.mitocode.exception.OrderNotFoundException;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.vo.OrderId;
import com.hexagonal.mitocode.port.in.GetOrderByIdUseCase;
import com.hexagonal.mitocode.port.out.FindOrderByIdPort;

import java.util.Optional;

public class GetOrderByIdService implements GetOrderByIdUseCase {

    private final FindOrderByIdPort findOrderByIdPort;

    public GetOrderByIdService(FindOrderByIdPort findOrderByIdPort) {
        this.findOrderByIdPort = findOrderByIdPort;
    }


    @Override
    public Order getOrderById(String orderId) {
        return findOrderByIdPort.findById(OrderId.of(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

    }
}
