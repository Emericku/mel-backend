package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.OrderError;
import fr.polytech.melusine.exceptions.errors.ProductError;
import fr.polytech.melusine.exceptions.errors.UserError;
import fr.polytech.melusine.models.Item;
import fr.polytech.melusine.models.dtos.requests.OrderRequest;
import fr.polytech.melusine.models.entities.Order;
import fr.polytech.melusine.models.entities.OrderItem;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.repositories.OrderItemRepository;
import fr.polytech.melusine.repositories.OrderRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import fr.polytech.melusine.repositories.UserRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final Clock clock;


    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        OrderItemRepository orderItemRepository, UserRepository userRepository, Clock clock) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }


    @Transactional
    public User createOrder(OrderRequest orderRequest) {
        log.info("Create order : " + orderRequest.getName());
        if (orderRequest.getItemList().isEmpty()) throw new BadRequestException(OrderError.INVALID_ORDER);
        String displayName = Strings.capitalize(orderRequest.getName().toLowerCase().trim());
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, orderRequest.getUserId()));
        Order order = Order.builder()
                .displayName(displayName)
                .user(user)
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .delivered(false)
                .build();
        Order savedOrder = orderRepository.save(order);

        long total = 0;
        for (Item item : orderRequest.getItemList()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException(ProductError.INVALID_PRODUCT_ID));
            OrderItem orderItem = OrderItem.builder()
                    .orderId(savedOrder.getId())
                    .price(product.getPrice())
                    .quantity(item.getQuantity())
                    .createdAt(OffsetDateTime.now(clock))
                    .updatedAt(OffsetDateTime.now(clock))
                    .build();
            total = total + orderItem.getQuantity() * orderItem.getPrice();
            orderItemRepository.save(orderItem);
        }

        Order updatedOrder = savedOrder.toBuilder()
                .total(total)
                .build();
        orderRepository.save(updatedOrder);

        User updatedUser = user.toBuilder()
                .credit(user.getCredit() - total)
                .updatedAt(OffsetDateTime.now(clock))
                .build();
        return userRepository.save(updatedUser);
    }

}
