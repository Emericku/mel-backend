package fr.polytech.melusine.models.dtos.requests;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class IngredientRequest {

    @NonNull
    private String name;

    private long price;

    private String image;

    private long quantity;

}
