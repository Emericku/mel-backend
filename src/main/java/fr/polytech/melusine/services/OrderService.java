package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.OrderError;
import fr.polytech.melusine.exceptions.errors.ProductError;
import fr.polytech.melusine.exceptions.errors.UserError;
import fr.polytech.melusine.mappers.OrderItemMapper;
import fr.polytech.melusine.mappers.OrderMapper;
import fr.polytech.melusine.models.Item;
import fr.polytech.melusine.models.dtos.requests.OrderItemRequest;
import fr.polytech.melusine.models.dtos.requests.OrderRequest;
import fr.polytech.melusine.models.dtos.responses.OrderItemResponse;
import fr.polytech.melusine.models.dtos.responses.OrderResponse;
import fr.polytech.melusine.models.entities.Order;
import fr.polytech.melusine.models.entities.OrderItem;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.OrderStatus;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final Clock clock;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            OrderItemMapper orderItemMapper,
            OrderMapper orderMapper,
            Clock clock)
    {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.orderItemMapper = orderItemMapper;
        this.orderMapper = orderMapper;
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
    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.debug("Create order : " + orderRequest.getName());

        if (orderRequest.getItems().isEmpty()) {
            throw new BadRequestException(OrderError.INVALID_ORDER);
        }

        String displayName = Strings.capitalize(orderRequest.getName().toLowerCase().trim());
        User user = null;

        if (Objects.nonNull(orderRequest.getUserId())) {
            user = findUserById(orderRequest.getUserId());
            ensureUserCreditIsUpperThanZero(user);
        }

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .displayName(displayName)
                .user(user)
                .total(0L)
                .status(OrderStatus.PENDING)
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        List<OrderItem> items = orderRequest.getItems().stream()
                .map(item -> saveOrderItem(order, item))
                .collect(Collectors.toList());

        long total = items.stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .mapToLong(Long::valueOf)
                .sum();

        Order savedOrder = order.toBuilder()
                .items(items)
                .total(total)
                .build();

        log.info("Order saved with ID : " + savedOrder.getId());

        updateUserCredit(user, total);

        Order createdOrder = orderRepository.save(savedOrder);
        log.debug("End of order creation");

        return orderMapper.mapToOrderResponse(createdOrder);
    }

    private void updateUserCredit(User user, long total) {
        if (Objects.nonNull(user)) {
            long newCredit = user.getCredit() - total;

            User updatedUser = user.toBuilder()
                    .credit(newCredit)
                    .updatedAt(OffsetDateTime.now(clock))
                    .build();

            userRepository.save(updatedUser);
            log.info("User saved with ID : " + updatedUser.getId() + " and new credit : " + newCredit);
        }
    }

    private OrderItem saveOrderItem(Order order, Item item) {
        Product product = findProductById(item.getProductId());

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .price(product.getPrice())
                .quantity(item.getQuantity())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .status(OrderStatus.PENDING)
                .build();

        log.info("Order item saved with order ID : " + order.getId());

        return orderItemRepository.save(orderItem);
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
    public OrderItem updateOrderStatus(String itemId, OrderItemRequest request) {
        log.debug("Cancel an item from with item id : " + itemId);

        OrderItem orderItem = findOrderItemById(itemId);
        ensureOrderItemIsPending(orderItem);

        Order order = findOrderById(orderItem.getOrder().getId());
        User user = findUserById(order.getUser().getId());

        OrderItem orderItemToUpdate = orderItem.toBuilder()
                .status(request.getStatus())
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        OrderItem updatedOrderItem = orderItemRepository.save(orderItemToUpdate);

        calculateAndSaveOrderStatus(order);

        long newCredit = user.getCredit() + orderItem.getPrice() * orderItem.getQuantity();

        User updatedUser = user.toBuilder()
                .credit(newCredit)
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        userRepository.save(updatedUser);

        log.info("End of cancel");
        return updatedOrderItem;
    }

    private void calculateAndSaveOrderStatus(Order order) {
        Order orderToUpdate = findOrderById(order.getId());

        boolean isPending = orderToUpdate.getItems().stream()
                .map(OrderItem::getStatus)
                .anyMatch(status -> status.equals(OrderStatus.PENDING));

        if (!isPending) {
            boolean isDeliver = orderToUpdate.getItems().stream()
                    .map(OrderItem::getStatus)
                    .anyMatch(status -> status.equals(OrderStatus.DELIVER));

            if (isDeliver) {
                Order updatedOrder = orderToUpdate.toBuilder()
                        .status(OrderStatus.DELIVER)
                        .updatedAt(OffsetDateTime.now(clock))
                        .build();

                orderRepository.save(updatedOrder);
            } else {
                Order updatedOrder = orderToUpdate.toBuilder()
                        .status(OrderStatus.CANCEL)
                        .updatedAt(OffsetDateTime.now(clock))
                        .build();

                orderRepository.save(updatedOrder);
            }
        } else {
            Order updatedOrder = orderToUpdate.toBuilder()
                    .updatedAt(OffsetDateTime.now(clock))
                    .build();

            orderRepository.save(updatedOrder);
        }
    }

    private void ensureOrderItemIsPending(OrderItem orderItem) {
        if (!orderItem.getStatus().equals(OrderStatus.PENDING)) {
            throw new BadRequestException(OrderError.ORDER_ITEM_IS_NOT_PENDING, orderItem.getId());
        }
    }

    public Page<OrderItemResponse> getOrderItems(Pageable pageable) {
        log.debug("Find all order items");

        return orderItemRepository.findAll(pageable).map(item -> {
            Order order = findOrderById(item.getOrder().getId());
            return orderItemMapper.mapToOrderItemResponse(item, order.getDisplayName());
        });
    }

}
