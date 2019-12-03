package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.entities.Ingredient;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponse {

    private String productId;

    private String name;

    private String category;

    private long price;

    private List<Ingredient> ingredients;

    private String image;

}