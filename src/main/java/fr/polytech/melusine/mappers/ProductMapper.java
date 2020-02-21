package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Product;
import org.springframework.stereotype.Component;

import static fr.polytech.melusine.utils.PriceFormatter.formatToDouble;

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
                .price(formatToDouble(product.getPrice()))
                .quantity(quantity)
                .ingredients(product.getIngredients())
                .build();
    }
    
}
