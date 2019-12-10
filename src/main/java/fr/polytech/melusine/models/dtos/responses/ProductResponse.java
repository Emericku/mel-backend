package fr.polytech.melusine.models.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductResponse {

    private String productId;

    private String name;

    private Category category;

    private double price;

    private long quantity;

    private List<Ingredient> ingredients;

    private String image;

}
