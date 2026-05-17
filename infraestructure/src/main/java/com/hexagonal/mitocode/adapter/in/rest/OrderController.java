package com.hexagonal.mitocode.adapter.in.rest;

import com.hexagonal.mitocode.adapter.in.rest.dto.AddItemRequest;
import com.hexagonal.mitocode.adapter.in.rest.dto.CreateOrderRequest;
import com.hexagonal.mitocode.adapter.in.rest.dto.OrderResponse;
import com.hexagonal.mitocode.adapter.in.rest.dto.OrderResponseMapper;
import com.hexagonal.mitocode.command.AddItemToOrderCommand;
import com.hexagonal.mitocode.command.CreateOrderCommand;
import com.hexagonal.mitocode.model.entity.Order;
import com.hexagonal.mitocode.port.in.AddItemToOrderUseCase;
import com.hexagonal.mitocode.port.in.CancelOrderUseCase;
import com.hexagonal.mitocode.port.in.CreateOrderUseCase;
import com.hexagonal.mitocode.port.in.PayOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final OrderResponseMapper responseMapper;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           AddItemToOrderUseCase addItemToOrderUseCase,
                           PayOrderUseCase payOrderUseCase,
                           CancelOrderUseCase cancelOrderUseCase,
                           OrderResponseMapper responseMapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.addItemToOrderUseCase = addItemToOrderUseCase;
        this.payOrderUseCase = payOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest request){

        Order order = createOrderUseCase.createOrder(new CreateOrderCommand(request.customerId()));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(order.getId().toString())
                .toUri();

        return ResponseEntity.created(location).body(responseMapper.toResponse(order));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderResponse> addItem(
            @PathVariable String id,
            @RequestBody AddItemRequest request){

        Order order = addItemToOrderUseCase.addItem(new AddItemToOrderCommand(
                id,
                request.productId(),
                request.productName(),
                request.quantity(),
                request.unitPrice(),
                request.currency()
        ));

        return ResponseEntity.ok(responseMapper.toResponse(order));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> payOrder(@PathVariable String id){
        payOrderUseCase.payOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable String id){
        cancelOrderUseCase.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }


}
