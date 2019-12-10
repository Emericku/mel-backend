package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponse {

    private String productId;

    private String name;

    private Category category; // ENUM

    private long price; // Reformat double

    // quantity

    private List<Ingredient> ingredients;

    private String image;

}
