package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.utils.MoneyFormatter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductMapper {

    /**
     * Map product object to a product response.
     *
     * @param product
     * @return
     */
    public ProductResponse mapProductToProductResponse(Product product, long quantity) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(Optional.ofNullable(product.getPrice()).map(MoneyFormatter::formatToDouble).orElse(null))
                .quantity(quantity)
                .ingredients(product.getIngredients())
                .build();
    }
    
}
