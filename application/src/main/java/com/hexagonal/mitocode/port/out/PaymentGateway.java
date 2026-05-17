package com.hexagonal.mitocode.port.out;

import com.hexagonal.mitocode.model.vo.Money;

/**
 * Interfaz que define el contrato para la pasarela de pago.
 */
public interface PaymentGateway {
    boolean processPayment(String orderId, Money amount);
}
