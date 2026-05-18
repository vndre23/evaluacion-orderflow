package com.hexagonal.mitocode.adapter.out.notification;

import com.hexagonal.mitocode.adapter.out.payment.SimulatedPaymentGateway;
import com.hexagonal.mitocode.model.enums.OrderStatus;
import com.hexagonal.mitocode.port.out.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimulatedNotificationService implements NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(SimulatedNotificationService.class);

    @Override
    public void notifyOrderStatusChange(String orderId, OrderStatus status) {

        log.info("Se envio una notificación a la Orden ID: {} con cambio de estado: {}", orderId, status);

    }
}
