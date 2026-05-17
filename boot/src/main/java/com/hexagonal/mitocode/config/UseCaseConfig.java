package com.hexagonal.mitocode.config;

import com.hexagonal.mitocode.port.in.AddItemToOrderUseCase;
import com.hexagonal.mitocode.port.in.CancelOrderUseCase;
import com.hexagonal.mitocode.port.in.CreateOrderUseCase;
import com.hexagonal.mitocode.port.in.PayOrderUseCase;
import com.hexagonal.mitocode.port.out.FindOrderByIdPort;
import com.hexagonal.mitocode.port.out.InventoryService;
import com.hexagonal.mitocode.port.out.PaymentGateway;
import com.hexagonal.mitocode.port.out.SaveOrderPort;
import com.hexagonal.mitocode.service.AddItemToOrderService;
import com.hexagonal.mitocode.service.CancelOrderService;
import com.hexagonal.mitocode.service.CreateOrderService;
import com.hexagonal.mitocode.service.PayOrderService;
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
                                           SaveOrderPort saveOrderPort) {
        return new PayOrderService(paymentGateway, findOrderByIdPort, saveOrderPort);
    }

    @Bean
    public CancelOrderUseCase cancelOrderUseCase(FindOrderByIdPort findOrderByIdPort,
                                                 SaveOrderPort saveOrderPort){
        return new CancelOrderService(findOrderByIdPort, saveOrderPort);
    }

}