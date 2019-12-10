package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    /**
     * Map product object to a product response.
     *
     * @param product
     * @return
     */
    public ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .ingredients(product.getIngredients())
                .build();
    }

    /**
     * Map a list of product to a list of product response.
     *
     * @param products
     * @return
     */
    public List<ProductResponse> mapProductsToProductsResponse(List<Product> products) {
        return products.stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

}
