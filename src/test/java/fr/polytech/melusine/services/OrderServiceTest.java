package fr.polytech.melusine.services;

import fr.polytech.melusine.TestData;
import fr.polytech.melusine.mappers.OrderItemMapper;
import fr.polytech.melusine.models.dtos.requests.OrderRequest;
import fr.polytech.melusine.repositories.OrderItemRepository;
import fr.polytech.melusine.repositories.OrderRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import fr.polytech.melusine.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.Mockito.when;

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

        OrderRequest request = OrderRequest.builder()
                .name("order")
                .items(List.of(TestData.ITEM_1))
                .userId(TestData.USER_BRUCE_WAYNE.getId())
                .build();

        orderService.createOrder(request);
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