package com.hexagonal.mitocode.service;

import com.hexagonal.mitocode.exception.OrderDomainException;
import com.hexagonal.mitocode.exception.OrderNotFoundException;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.model.vo.OrderId;
import com.hexagonal.mitocode.port.in.PayOrderUseCase;
import com.hexagonal.mitocode.port.out.FindOrderByIdPort;
import com.hexagonal.mitocode.port.out.PaymentGateway;
import com.hexagonal.mitocode.port.out.SaveOrderPort;

/*
 * Servicio que implementa la lógica para pagar una orden.
 * Este servicio interactúa con la pasarela de pago.
 */
public class PayOrderService implements PayOrderUseCase {

    private final PaymentGateway paymentGateway;
    private final FindOrderByIdPort findOrderByIdPort;
    private final SaveOrderPort saveOrderPort;

    public PayOrderService(PaymentGateway paymentGateway,
                           FindOrderByIdPort findOrderByIdPort,
                           SaveOrderPort saveOrderPort) {
        this.paymentGateway = paymentGateway;
        this.findOrderByIdPort = findOrderByIdPort;
        this.saveOrderPort = saveOrderPort;
    }

    @Override
    public void payOrder(String orderId) {
        Order order = findOrderByIdPort.findById(OrderId.of(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.calculateTotal();
        boolean success = paymentGateway.processPayment(orderId, order.getTotal());
        if (!success)
            throw new OrderDomainException( "Payment processing failed for order: " + orderId);

        order.pay();
        saveOrderPort.save(order);
    }

}
