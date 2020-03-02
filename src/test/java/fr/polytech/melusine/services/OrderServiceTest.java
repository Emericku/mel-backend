package fr.polytech.melusine.services;

import fr.polytech.melusine.mappers.OrderItemMapper;
import fr.polytech.melusine.mappers.OrderMapper;
import fr.polytech.melusine.repositories.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private Clock clock;

    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderService(orderRepository, productRepository, orderItemRepository, userRepository, orderItemMapper, orderMapper, ingredientRepository, clock);
    }

    /**
     * TODO
     *
     * @Test public void createOrder() {
     * User user = TestData.USER_BRUCE_WAYNE;
     * OffsetDateTime now = OffsetDateTime.now();
     * <p>
     * OrderRequest request = OrderRequest.builder()
     * .name("displayName")
     * .items(List.of(TestData.ITEM_1.getProductId()))
     * .userId(user.getId())
     * .build();
     * <p>
     * Order orderToSave = Order.builder()
     * .clientName("Displayname")
     * .user(user)
     * .createdAt(TestData.INSTANT_1)
     * .updatedAt(TestData.INSTANT_1)
     * .build();
     * <p>
     * Product product = TestData.PRODUCT_1;
     * <p>
     * when(clock.instant()).thenReturn(now.toInstant());
     * when(clock.getZone()).thenReturn(ZoneId.of("Europe/Paris"));
     * <p>
     * when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
     * when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
     * when(orderItemRepository.save(any(OrderItem.class))).then(returnsFirstArg());
     * when(orderRepository.save(any(Order.class))).then(returnsFirstArg());
     * when(orderMapper.mapToOrderResponse(any(Order.class))).thenCallRealMethod();
     * <p>
     * OrderResponse response = orderService.createOrder(request);
     * <p>
     * assertThat(response).isNotNull();
     * assertThat(response.getTotal()).isEqualTo(1.2);
     * assertThat(response.getCreatedAt()).isEqualTo(now);
     * assertThat(response.getUpdatedAt()).isEqualTo(now);
     * <p>
     * ArgumentCaptor<OrderItem> orderItemCaptor = ArgumentCaptor.forClass(OrderItem.class);
     * verify(orderItemRepository).save(orderItemCaptor.capture());
     * <p>
     * ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
     * verify(orderRepository).save(orderCaptor.capture());
     * <p>
     * ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
     * verify(userRepository).save(userCaptor.capture());
     * <p>
     * assertThat(orderItemCaptor.getValue().getPrice()).isEqualTo(120L);
     * <p>
     * assertThat(orderCaptor.getValue().getClientName()).isEqualTo(orderToSave.getClientName());
     * assertThat(orderCaptor.getValue().getUser()).isEqualTo(user);
     * assertThat(orderCaptor.getValue().getTotal()).isEqualTo(120);
     * <p>
     * assertThat(userCaptor.getValue().getId()).isEqualTo(user.getId());
     * assertThat(userCaptor.getValue().getCredit()).isEqualTo(user.getCredit() - orderCaptor.getValue().getTotal());
     * }
     **/
    @Test
    public void cancelOrderItem() {
    }

    @Test
    public void deliverOrderItem() {
    }

    @Test
    public void getOrderItems() {
    }
}