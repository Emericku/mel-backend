package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.enums.IngredientType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientResponse {

    private String id;

    private String name;

    private long price;

    private long quantity;

    private String image;

    private IngredientType type;

}
