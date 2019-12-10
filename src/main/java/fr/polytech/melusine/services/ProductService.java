package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.CreditError;
import fr.polytech.melusine.exceptions.errors.ProductError;
import fr.polytech.melusine.mappers.ProductMapper;
import fr.polytech.melusine.models.dtos.requests.ProductRequest;
import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.models.enums.Category;
import fr.polytech.melusine.repositories.IngredientRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductMapper productMapper;
    private final Clock clock;

    public ProductService(ProductRepository productRepository, IngredientRepository ingredientRepository, ProductMapper productMapper, Clock clock) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.productMapper = productMapper;
        this.clock = clock;
    }

    public void createProduct(ProductRequest productRequest) {
        log.info("Create product : " + productRequest.getName());
        if (productRepository.existsByNameAndIsOriginalTrue(productRequest.getName())) {
            throw new ConflictException(ProductError.CONFLICT, productRequest.getName());
        }

        long price = productRequest.getPrice();
        ensurePriceUpperThanZero(price);

        String name = Strings.capitalize(productRequest.getName().toLowerCase().trim());
        Category category = productRequest.getCategory();
        List<String> ingredientRequest = productRequest.getIngredients();
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        if (Objects.nonNull(ingredientRequest) && !ingredientRequest.isEmpty()) {
            ingredients.addAll(ingredientRequest.stream()
                    .map(ingredientName -> ingredientRepository.findByName(ingredientName)
                            .orElseThrow(() -> new NotFoundException(ProductError.INVALID_PRODUCT_NAME, ingredientName)))
                    .collect(Collectors.toList()));
            for (Ingredient ingredient : ingredients) {
                price = price + ingredient.getPrice();
            }
        }

        Product product = Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .isOriginal(productRequest.isOriginal())
                .image(productRequest.getImage())
                .ingredients(ingredients)
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        productRepository.save(product);
        log.info("Creation success : " + productRequest.getName() + " category : " + category);
    }

    public Page<ProductResponse> getProducts(Pageable pageable) {
        log.debug("Find product by page");
        Page<Product> productPages = productRepository.findAll(pageable);

        return productPages.map(productMapper::mapProductToProductResponse);
    }

    public ProductResponse getProduct(String productId) {
        log.debug("Find product by id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ProductError.INVALID_PRODUCT_ID));
        return productMapper.mapProductToProductResponse(product);
    }

    public Product updateProduct(String productId, ProductRequest productRequest) {
        log.debug("Update product by id: {}", productId);
        ensurePriceUpperThanZero(productRequest.getPrice());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ProductError.INVALID_PRODUCT_ID));

        String name = productRequest.getName().isEmpty() ? product.getName() : productRequest.getName();
        String image = productRequest.getImage().isEmpty() || !Objects.nonNull(productRequest.getImage()) ? product.getImage() : productRequest.getImage();

        Product updatedProduct = product.toBuilder()
                .name(name)
                .price(product.getPrice())
                .image(image)
                .build();

        return productRepository.save(updatedProduct);
    }

    private void ensurePriceUpperThanZero(long price) {
        if (price <= 0)
            throw new BadRequestException(CreditError.INVALID_CREDIT, price);
    }

}
