package fr.polytech.melusine.services;

import fr.polytech.melusine.configurations.Constants;
import fr.polytech.melusine.exceptions.ConflictException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.ProductError;
import fr.polytech.melusine.mappers.ProductMapper;
import fr.polytech.melusine.models.dtos.requests.ProductRequest;
import fr.polytech.melusine.models.dtos.responses.CategoryResponse;
import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.models.enums.Category;
import fr.polytech.melusine.repositories.IngredientRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    public static final String PAIN = "Pain";
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
    public ProductResponse createProduct(ProductRequest productRequest) {
        String name = Strings.capitalize(productRequest.getName().toLowerCase().trim());
        log.info("Create a product with name : " + name);

        if (productRepository.existsByNameAndIsOriginalTrue(name)) {
            throw new ConflictException(ProductError.CONFLICT, productRequest.getName());
        }

        List<Ingredient> ingredients = Optional.ofNullable(productRequest.getIngredients())
                .filter(Predicate.not(List::isEmpty))
                .map(ingredientRepository::findByIdIn)
                .orElse(List.of());

        long ingredientsPrice = ingredients.stream()
                .map(Ingredient::getPrice)
                .mapToLong(Long::longValue)
                .sum();

        long price = Objects.nonNull(productRequest.getPrice()) ? productRequest.getPrice() : ingredientsPrice;

        Product product = Product.builder()
                .name(name)
                .category(productRequest.getCategory())
                .price(price)
                .isOriginal(productRequest.isOriginal())
                .ingredients(ingredients)
                .image(productRequest.getImage())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        log.info("End of product's creation with name : " + productRequest.getName() + " and category : " + productRequest.getCategory());
        Product createdProduct = productRepository.save(product);
        return productMapper.mapProductToProductResponse(createdProduct, 1);
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
        return getProductResponse(product);
    }

    /**
     * Update a product.
     *
     * @param productRequest the request
     * @return the product
     */
    public Product updateProduct(String id, ProductRequest productRequest) {
        log.debug("Update product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProductError.NOT_FOUND, id));

        String name = productRequest.getName().isEmpty() ? product.getName() : productRequest.getName();

        Product updatedProduct = product.toBuilder()
                .name(name)
                .price(product.getPrice())
                .image(productRequest.getImage())
                .build();


        log.info("End of update a product");
        return productRepository.save(updatedProduct);
    }

    /**
     * Get all products.
     *
     * @return a list of products response
     */
    public List<ProductResponse> getProducts() {
        log.debug("Find all products by original true");
        List<Product> products = productRepository.findByIsOriginalTrue();

        return products.stream()
                .map(this::getProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse getProductResponse(Product product) {
        Optional<Long> optionalQuantity = product.getIngredients().stream()
                .min(Comparator.comparingLong(Ingredient::getQuantity))
                .map(Ingredient::getQuantity);

        if (optionalQuantity.isPresent())
            return productMapper.mapProductToProductResponse(product, optionalQuantity.get());

        Ingredient pain = ingredientRepository.findByName(PAIN);
        return productMapper.mapProductToProductResponse(product, pain.getQuantity());
    }

    public List<CategoryResponse> getCategories() {
        log.debug("Find all products by original true");
        List<Category> categories = List.of(Category.values());
        return categories.stream()
                .map(this::getCategoryResponse)
                .collect(Collectors.toList());

    }

    private CategoryResponse getCategoryResponse(Category category) {
        CategoryResponse response = CategoryResponse.builder()
                .name(category)
                .build();
        if (category.equals(Category.FROID)) {
            return response.toBuilder()
                    .icon(Constants.ICON_FROID_B64)
                    .color("#26A1CE")
                    .build();
        }
        if (category.equals(Category.CHAUD)) {
            return response.toBuilder()
                    .icon(Constants.ICON_CHAUD_B64)
                    .color("#ED6E35")
                    .build();
        }
        if (category.equals(Category.CUSTOM)) {
            return response.toBuilder()
                    .icon(Constants.ICON_CUSTOM_B64)
                    .color("#EAB72E")
                    .build();
        }
        if (category.equals(Category.BOISSON)) {
            return response.toBuilder()
                    .icon(Constants.ICON_BOISSOIN_B64)
                    .color("#E82D67")
                    .build();
        }
        if (category.equals(Category.DESSERT)) {
            return response.toBuilder()
                    .icon(Constants.ICON_DESSERT_B64)
                    .color("#9A2AE0")
                    .build();
        }
        return null;
    }
}
