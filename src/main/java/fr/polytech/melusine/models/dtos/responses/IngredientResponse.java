package fr.polytech.melusine.models.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class IngredientResponse {

    private String ingredientId;

    private String name;

    private long price;

    private long quantity;

    private String image;

}
