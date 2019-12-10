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


    /**
     * Creation of an order.
     *
     * @param orderRequest the request
     * @return the order
     */
    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        log.debug("Create order : " + orderRequest.getName());
        if (orderRequest.getItems().isEmpty()) throw new BadRequestException(OrderError.INVALID_ORDER);
        String displayName = Strings.capitalize(orderRequest.getName().toLowerCase().trim());
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, orderRequest.getUserId()));
        ensureUserCreditIsUpperThanZero(user);

        Order order = Order.builder()
                .displayName(displayName)
                .user(user)
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();
        Order savedOrder = orderRepository.save(order);

        long total = 0;
        for (Item item : orderRequest.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException(ProductError.INVALID_PRODUCT_ID));
            OrderItem orderItem = OrderItem.builder()
                    .orderId(savedOrder.getId())
                    .price(product.getPrice())
                    .quantity(item.getQuantity())
                    .createdAt(OffsetDateTime.now(clock))
                    .updatedAt(OffsetDateTime.now(clock))
                    .delivered(false)
                    .cancelled(false)
                    .build();
            total = total + orderItem.getQuantity() * orderItem.getPrice();
            orderItemRepository.save(orderItem);
        }

        Order updatedOrder = savedOrder.toBuilder()
                .total(total)
                .build();
        orderRepository.save(updatedOrder);
        log.info("Order saved with ID : " + updatedOrder.getId());

        long newCredit = user.getCredit() - total;
        User updatedUser = user.toBuilder()
                .credit(newCredit)
                .updatedAt(OffsetDateTime.now(clock))
                .build();
        userRepository.save(updatedUser);
        log.info("User saved with ID : " + updatedUser.getId() + " and new credit = " + newCredit);

        return updatedOrder;
    }

    private void ensureUserCreditIsUpperThanZero(User user) {
        if (user.getCredit() <= 0) {
            throw new BadRequestException(UserError.USER_CREDIT_UNDER_ZERO, user.getId());
        }
    }


    /**
     * Cancel an item from an order.
     *
     * @param itemId the item id
     * @return an order item
     */
    public OrderItem cancelOrderItem(String itemId) {
        log.info("Remove an item with id : " + itemId);
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(OrderError.ORDER_ITEM_NOT_FOUND, itemId));

        ensureOrderItemIsNotDelivered(orderItem);
        ensureOrderItemIsNotCancelled(orderItem);

        OrderItem updatedOrderItem = orderItem.toBuilder()
                .cancelled(true)
                .build();

        return orderItemRepository.save(updatedOrderItem);
    }

    /**
     * Deliver an item from an order.
     *
     * @param itemId the item id
     * @return an order item
     */
    public OrderItem deliverOrderItem(String itemId) {
        log.info("Deliver an item with id : " + itemId);
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(OrderError.ORDER_ITEM_NOT_FOUND, itemId));

        ensureOrderItemIsNotDelivered(orderItem);
        ensureOrderItemIsNotCancelled(orderItem);

        OrderItem updatedOrderItem = orderItem.toBuilder()
                .delivered(true)
                .build();

        return orderItemRepository.save(updatedOrderItem);
    }

    private void ensureOrderItemIsNotCancelled(OrderItem orderItem) {
        if (orderItem.isCancelled()) {
            throw new BadRequestException(OrderError.ORDER_ITEM_ALREADY_CANCELLED, orderItem.getId());
        }
    }

    private void ensureOrderItemIsNotDelivered(OrderItem orderItem) {
        if (orderItem.isDelivered()) {
            throw new BadRequestException(OrderError.ORDER_ITEM_ALREADY_DELIVERED, orderItem.getId());
        }
    }

}
