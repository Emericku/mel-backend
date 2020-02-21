package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.OrderError;
import fr.polytech.melusine.exceptions.errors.ProductError;
import fr.polytech.melusine.exceptions.errors.UserError;
import fr.polytech.melusine.mappers.OrderItemMapper;
import fr.polytech.melusine.models.Item;
import fr.polytech.melusine.models.dtos.requests.OrderItemRequest;
import fr.polytech.melusine.models.dtos.requests.OrderRequest;
import fr.polytech.melusine.models.dtos.responses.OrderItemResponse;
import fr.polytech.melusine.models.entities.Order;
import fr.polytech.melusine.models.entities.OrderItem;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.ValidationStatus;
import fr.polytech.melusine.repositories.OrderItemRepository;
import fr.polytech.melusine.repositories.OrderRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import fr.polytech.melusine.repositories.UserRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderItemMapper orderItemMapper;
    private final Clock clock;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        OrderItemRepository orderItemRepository, UserRepository userRepository, OrderItemMapper orderItemMapper, Clock clock) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.orderItemMapper = orderItemMapper;
        this.clock = clock;
    }

    private User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND, id));
    }

    private OrderItem findOrderItemById(String id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(OrderError.ORDER_ITEM_NOT_FOUND, id));
    }

    private Order findOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(OrderError.ORDER_NOT_FOUND, id));
    }

    private Product findProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProductError.INVALID_NAME, id));
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
        User user = findUserById(orderRequest.getUserId());
        ensureUserCreditIsUpperThanZero(user);

        String orderId = UUID.randomUUID().toString();


        long total = orderRequest.getItems().stream()
                .map(item -> getPriceForItems(orderId, item))
                .mapToLong(Long::valueOf)
                .sum();

        Order order = Order.builder()
                .id(orderId)
                .displayName(displayName)
                .user(user)
                .total(total)
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        orderRepository.save(order);
        log.info("Order saved with ID : " + order.getId());

        long newCredit = user.getCredit() - total;
        User updatedUser = user.toBuilder()
                .credit(newCredit)
                .updatedAt(OffsetDateTime.now(clock))
                .build();
        userRepository.save(updatedUser);
        log.info("User saved with ID : " + updatedUser.getId() + " and new credit = " + newCredit);

        return order;
    }

    private Long getPriceForItems(String orderId, Item item) {
        Product product = findProductById(item.getProductId());
        OrderItem orderItem = OrderItem.builder()
                .orderId(orderId)
                .price(product.getPrice())
                .quantity(item.getQuantity())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .status(ValidationStatus.PENDING)
                .build();
        orderItemRepository.save(orderItem);
        log.info("Order item saved with order ID : " + orderId);
        return item.getQuantity() * product.getPrice();
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
    public OrderItem updateValidationStatus(String itemId, OrderItemRequest request) {
        log.debug("Cancel an item from with item id : " + itemId);
        OrderItem orderItem = findOrderItemById(itemId);
        ensureOrderItemIsPending(orderItem);
        Order order = findOrderById(orderItem.getOrderId());
        User user = findUserById(order.getUser().getId());

        Order updatedOrder = order.toBuilder()
                .updatedAt(OffsetDateTime.now(clock))
                .build();
        orderRepository.save(updatedOrder);

        OrderItem updatedOrderItem = orderItem.toBuilder()
                .status(request.getStatus())
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        OrderItem savedOrder = orderItemRepository.save(updatedOrderItem);

        long newCredit = user.getCredit() + orderItem.getPrice() * orderItem.getQuantity();
        User updatedUser = user.toBuilder()
                .credit(newCredit)
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        userRepository.save(updatedUser);

        log.info("End of cancel");
        return savedOrder;
    }

    private void ensureOrderItemIsPending(OrderItem orderItem) {
        if (!orderItem.getStatus().equals(ValidationStatus.PENDING)) {
            throw new BadRequestException(OrderError.ORDER_ITEM_IS_NOT_PENDING, orderItem.getId());
        }
    }

    public Page<OrderItemResponse> getOrderItems(Pageable pageable) {
        log.debug("Find all order items");
        Page<OrderItem> orderItems = orderItemRepository.findAll(pageable);
        return orderItems.map(item -> {
            Order order = findOrderById(item.getOrderId());
            return orderItemMapper.mapToOrderItemResponse(item, order.getDisplayName());
        });
    }

}
