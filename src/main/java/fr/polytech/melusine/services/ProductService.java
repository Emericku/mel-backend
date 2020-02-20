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
import java.util.List;
import java.util.Objects;

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

    /**
     * Create a product.
     *
     * @param productRequest the request
     * @return a product
     */
    public Product createProduct(ProductRequest productRequest) {
        String name = Strings.capitalize(productRequest.getName().toLowerCase().trim());
        log.info("Create a product with name : " + name);

        if (productRepository.existsByNameAndIsOriginalTrue(name)) {
            throw new ConflictException(ProductError.CONFLICT, productRequest.getName());
        }
        ensurePriceUpperThanZero(productRequest.getPrice());

        Category category = productRequest.getCategory();
        List<Ingredient> ingredients = List.of();
        if (Objects.nonNull(productRequest.getIngredients()) && !productRequest.getIngredients().isEmpty()) {
            ingredients = ingredientRepository.findByIdIn(productRequest.getIngredients());
        }

        long ingredientsPrice = ingredients.stream()
                .map(Ingredient::getPrice)
                .mapToLong(Long::longValue)
                .sum();

        long price = productRequest.getPrice() + ingredientsPrice;

        Product product = Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .isOriginal(productRequest.isOriginal())
                .ingredients(ingredients)
                .image(productRequest.getImage())
                .quantity(productRequest.getQuantity())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        log.info("End of product's creation with name : " + productRequest.getName() + " and category : " + category);
        return productRepository.save(product);

    }

    @Deprecated
    public Page<ProductResponse> getProducts(Pageable pageable) {
        log.debug("Find product by page");
        Page<Product> productPages = productRepository.findAll(pageable);
        return productPages.map(productMapper::mapProductToProductResponse);
    }

    /**
     * Get a product by his id.
     *
     * @param productId the product id
     * @return a product response
     */
    public ProductResponse getProduct(String productId) {
        log.debug("Find product by id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ProductError.NOT_FOUND, productId));
        return productMapper.mapProductToProductResponse(product);
    }

    /**
     * Update a product.
     *
     * @param productRequest the request
     * @return the product
     */
    public Product updateProduct(ProductRequest productRequest) {
        log.debug("Update product by id: {}", productRequest.getId());
        ensurePriceUpperThanZero(productRequest.getPrice());
        Product product = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new NotFoundException(ProductError.NOT_FOUND, productRequest.getId()));

        String name = productRequest.getName().isEmpty() ? product.getName() : productRequest.getName();

        Product updatedProduct = product.toBuilder()
                .name(name)
                .price(product.getPrice())
                .image(productRequest.getImage())
                .quantity(productRequest.getQuantity())
                .build();


        log.info("End of update a product");
        return productRepository.save(updatedProduct);
    }

    private void ensurePriceUpperThanZero(long price) {
        if (price <= 0)
            throw new BadRequestException(CreditError.INVALID_CREDIT, price);
    }

    /**
     * Get all products.
     *
     * @return a list of products response
     */
    public List<ProductResponse> getProducts() {
        log.debug("Find all products");
        List<Product> products = productRepository.findAll();
        return productMapper.mapProductsToProductsResponse(products);
    }

}
