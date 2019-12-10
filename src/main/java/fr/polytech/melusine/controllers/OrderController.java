package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.OrderRequest;
import fr.polytech.melusine.models.entities.Order;
import fr.polytech.melusine.models.entities.OrderItem;
import fr.polytech.melusine.services.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/orders", produces = "application/json; charset=UTF-8")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PostMapping(path = "/cancel/{itemId}")
    public OrderItem cancelOrderItem(@PathVariable String itemId) {
        return orderService.cancelOrderItem(itemId);
    }

    @PostMapping(path = "/deliver/{itemId}")
    public OrderItem deliverOrderItem(@PathVariable String itemId) {
        return orderService.deliverOrderItem(itemId);
    }

}
