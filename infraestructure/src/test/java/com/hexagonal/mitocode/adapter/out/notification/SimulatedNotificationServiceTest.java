package com.hexagonal.mitocode.adapter.out.notification;

import com.hexagonal.mitocode.adapter.out.payment.SimulatedPaymentGateway;
import com.hexagonal.mitocode.model.enums.OrderStatus;
import com.hexagonal.mitocode.model.vo.Money;
import com.hexagonal.mitocode.model.vo.OrderId;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SimulatedNotificationServiceTest {

    private final SimulatedNotificationService service = new SimulatedNotificationService();

    @Test
    void should_send_notification_successfully() {

        String orderId = OrderId.generate().toString();
        OrderStatus status = OrderStatus.CANCELLED;

        assertDoesNotThrow(() -> service.notifyOrderStatusChange(orderId, status));
    }
}
