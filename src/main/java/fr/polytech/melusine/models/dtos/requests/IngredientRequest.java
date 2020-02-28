package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.enums.IngredientType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class IngredientRequest {

    @NotEmpty
    private String name;

    @NotNull
    private Double price;

    private IngredientType type;

    private long quantity;

    private String image;

}
