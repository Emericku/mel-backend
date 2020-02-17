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
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductMapper productMapper;
    private final ImageManager imageManager;
    private final Clock clock;

    public ProductService(ProductRepository productRepository, IngredientRepository ingredientRepository, ProductMapper productMapper, ImageManager imageManager, Clock clock) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.productMapper = productMapper;
        this.imageManager = imageManager;
        this.clock = clock;
    }

    /**
     * Create a product.
     *
     * @param productRequest the request
     * @param image          the image file
     * @return a product
     */
    public Product createProduct(ProductRequest productRequest, MultipartFile image) {
        log.info("Create a product with name : " + productRequest.getName());
        if (productRepository.existsByNameAndIsOriginalTrue(productRequest.getName())) {
            throw new ConflictException(ProductError.CONFLICT, productRequest.getName());
        }

        long productPrice = productRequest.getPrice();
        ensurePriceUpperThanZero(productPrice);

        String name = Strings.capitalize(productRequest.getName().toLowerCase().trim());
        Category category = productRequest.getCategory();
        List<String> ingredientRequest = productRequest.getIngredients();

        List<Ingredient> ingredients = ingredientRequest.stream()
                .map(ingredientName -> ingredientRepository.findByName(ingredientName)
                        .orElseThrow(() -> new NotFoundException(ProductError.INVALID_NAME, ingredientName)))
                .collect(Collectors.toList());

        long ingredientsPrice = ingredients.stream()
                .map(Ingredient::getPrice)
                .mapToLong(Long::longValue)
                .sum();

        long price = productPrice + ingredientsPrice;

        String imagePath = null;
        if (Objects.nonNull(image)) {
            imagePath = imageManager.uploadImage(image);
        }

        Product product = Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .isOriginal(productRequest.isOriginal())
                .ingredients(ingredients)
                .image(imagePath)
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
     * @param productId      the product ID
     * @param productRequest the request
     * @param image          the image file
     * @return the product
     */
    public Product updateProduct(String productId, ProductRequest productRequest, MultipartFile image) {
        log.debug("Update product by id: {}", productId);
        ensurePriceUpperThanZero(productRequest.getPrice());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ProductError.NOT_FOUND, productId));

        String name = productRequest.getName().isEmpty() ? product.getName() : productRequest.getName();

        String imagePath = null;
        if (Objects.nonNull(image)) {
            imagePath = imageManager.uploadImage(image);
        }
        Product updatedProduct = product.toBuilder()
                .name(name)
                .price(product.getPrice())
                .image(imagePath)
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
