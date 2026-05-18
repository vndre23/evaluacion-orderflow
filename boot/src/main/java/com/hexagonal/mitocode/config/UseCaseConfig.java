package com.hexagonal.mitocode.config;

import com.hexagonal.mitocode.port.in.*;
import com.hexagonal.mitocode.port.out.*;
import com.hexagonal.mitocode.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(SaveOrderPort port) {
        return new CreateOrderService(port);
    }

    @Bean
    public AddItemToOrderUseCase addItemToOrderUseCase(FindOrderByIdPort findOrderByIdPort,
                                                       InventoryService inventory,
                                                       SaveOrderPort saveOrderPort) {
        return new AddItemToOrderService(findOrderByIdPort, inventory, saveOrderPort);
    }

    @Bean
    public PayOrderUseCase payOrderUseCase(PaymentGateway paymentGateway,
                                           FindOrderByIdPort findOrderByIdPort,
                                           SaveOrderPort saveOrderPort,
                                           NotificationService notificationService) {
        return new PayOrderService(paymentGateway, findOrderByIdPort, saveOrderPort, notificationService);
    }

    @Bean
    public CancelOrderUseCase cancelOrderUseCase(FindOrderByIdPort findOrderByIdPort,
                                                 SaveOrderPort saveOrderPort,
                                                 NotificationService notificationService){
        return new CancelOrderService(findOrderByIdPort, saveOrderPort, notificationService);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(FindOrderByIdPort findOrderByIdPort) {
        return new GetOrderByIdService(findOrderByIdPort);
    }

}