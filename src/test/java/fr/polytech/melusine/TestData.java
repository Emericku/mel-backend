package fr.polytech.melusine;

import fr.polytech.melusine.models.Item;
import fr.polytech.melusine.models.dtos.requests.AccountRequest;
import fr.polytech.melusine.models.entities.*;
import fr.polytech.melusine.models.enums.Category;
import fr.polytech.melusine.models.enums.Section;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class TestData {

    public static final OffsetDateTime INSTANT_1 = OffsetDateTime.of(2020, 02, 17, 13, 06, 0, 0, ZoneOffset.UTC);

    public static final OffsetDateTime INSTANT_2 = OffsetDateTime.of(2020, 02, 20, 13, 06, 0, 0, ZoneOffset.UTC);

    public static final Ingredient INGREDIENT_CHEESE = Ingredient.builder()
            .id("ingredientId")
            .name("cheese")
            .price(1)
            .quantity(20)
            .image("blarf")
            .createdAt(INSTANT_1)
            .updatedAt(INSTANT_1)
            .build();

    public static final User USER_BRUCE_WAYNE = User.builder()
            .id("userId")
            .firstName("Burce")
            .lastName("Wayne")
            .nickName("Batman")
            .section(Section.EXTERNAL)
            .credit(1000)
            .isMembership(true)
            .createdAt(INSTANT_1)
            .updatedAt(INSTANT_1)
            .build();

    public static final Account ACCOUNT_BRUCE_WAYNE = Account.builder()
            .id("accountId")
            .email("brce.wayne@gmail.com")
            .password("batman")
            .isBarman(false)
            .user(TestData.USER_BRUCE_WAYNE)
            .createdAt(INSTANT_1)
            .updatedAt(INSTANT_1)
            .build();

    public static final AccountRequest ACCOUNT_REQUEST_BRUCE_WAYNE = AccountRequest.builder()
            .clientId("clientId")
            .email("bruce.wayne@gmail.com")
            .isBarman(true)
            .password("batman")
            .build();

    public static final Item ITEM_1 = Item.builder()
            .productId("productId")
            .quantity(1)
            .build();

    public static final Order ODER_1 = Order.builder()
            .id("orderId")
            .displayName("displayName")
            .user(USER_BRUCE_WAYNE)
            .createdAt(INSTANT_1)
            .updatedAt(INSTANT_1)
            .build();

    public static final Product PRODUCT_1 = Product.builder()
            .id("productId")
            .name("Mitch")
            .category(Category.FROID)
            .price(120)
            .isOriginal(true)
            .ingredients(List.of(INGREDIENT_CHEESE))
            .createdAt(INSTANT_1)
            .updatedAt(INSTANT_1)
            .build();

}
