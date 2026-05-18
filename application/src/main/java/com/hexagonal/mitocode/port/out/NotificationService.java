package com.hexagonal.mitocode.port.out;

import com.hexagonal.mitocode.model.enums.OrderStatus;

public interface NotificationService {

    void notifyOrderStatusChange(String orderId, OrderStatus status);
}
