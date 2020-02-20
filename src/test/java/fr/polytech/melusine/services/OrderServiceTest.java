package fr.polytech.melusine.services;

import fr.polytech.melusine.TestData;
import fr.polytech.melusine.mappers.OrderItemMapper;
import fr.polytech.melusine.models.dtos.requests.OrderRequest;
import fr.polytech.melusine.models.entities.Order;
import fr.polytech.melusine.models.entities.OrderItem;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.repositories.OrderItemRepository;
import fr.polytech.melusine.repositories.OrderRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import fr.polytech.melusine.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private OrderItemMapper orderItemMapper;
    @Mock
    private Clock clock;

    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        when(clock.instant()).thenReturn(TestData.INSTANT_1.toInstant());
        orderService = new OrderService(orderRepository, productRepository, orderItemRepository, userRepository, orderItemMapper, clock);
    }

    @Test
    public void createOrder() {
        User user = TestData.USER_BRUCE_WAYNE;
        OrderRequest request = OrderRequest.builder()
                .name("displayName")
                .items(List.of(TestData.ITEM_1))
                .userId(user.getId())
                .build();
        Order orderToSave = Order.builder()
                .displayName("Displayname")
                .user(user)
                .createdAt(TestData.INSTANT_1)
                .updatedAt(TestData.INSTANT_1)
                .build();

        Order order = TestData.ODER_1;
        Product product = TestData.PRODUCT_1;

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        orderService.createOrder(request);

        ArgumentCaptor<OrderItem> orderItemCaptor = ArgumentCaptor.forClass(OrderItem.class);
        verify(orderItemRepository).save(orderItemCaptor.capture());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        assertThat(orderItemCaptor.getValue().getQuantity()).isEqualTo(1L);
        assertThat(orderItemCaptor.getValue().getPrice()).isEqualTo(120L);

        assertThat(orderCaptor.getValue().getDisplayName()).isEqualTo(orderToSave.getDisplayName());
        assertThat(orderCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(orderCaptor.getValue().getTotal()).isEqualTo(120);

        assertThat(userCaptor.getValue().getId()).isEqualTo(user.getId());
        assertThat(userCaptor.getValue().getCredit()).isEqualTo(user.getCredit() - orderCaptor.getValue().getTotal());
    }

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