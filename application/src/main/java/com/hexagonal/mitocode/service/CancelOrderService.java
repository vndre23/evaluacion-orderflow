package com.hexagonal.mitocode.service;

import com.hexagonal.mitocode.exception.OrderNotFoundException;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.vo.OrderId;
import com.hexagonal.mitocode.port.in.CancelOrderUseCase;
import com.hexagonal.mitocode.port.out.FindOrderByIdPort;
import com.hexagonal.mitocode.port.out.NotificationService;
import com.hexagonal.mitocode.port.out.SaveOrderPort;

/*
 * Servicio que implementa la lógica para cancelar una orden.
 * Este servicio se encarga de actualizarlo a "cancelada".
 */
public class CancelOrderService implements CancelOrderUseCase {

    private final FindOrderByIdPort findOrderByIdPort;
    private final SaveOrderPort saveOrderPort;

    private final NotificationService notificationService;

    public CancelOrderService(FindOrderByIdPort findOrderByIdPort,
                              SaveOrderPort saveOrderPort, NotificationService notificationService) {
        this.findOrderByIdPort = findOrderByIdPort;
        this.saveOrderPort = saveOrderPort;
        this.notificationService = notificationService;
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = findOrderByIdPort.findById(OrderId.of(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.cancel();
        saveOrderPort.save(order);

        notificationService.notifyOrderStatusChange(order.getId().toString(), order.getStatus());
    }

}
