package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.IngredientType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class IngredientRequest {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private long price;

    private IngredientType type;

    private long quantity;

    private String image;

}
