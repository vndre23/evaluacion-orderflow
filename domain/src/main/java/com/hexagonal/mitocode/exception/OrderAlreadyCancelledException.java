package com.hexagonal.mitocode.exception;

public class OrderAlreadyCancelledException extends OrderDomainException {

    public OrderAlreadyCancelledException(String orderId) {
        super("Order with id [" + orderId + "] has already been cancelled.");
    }

}
