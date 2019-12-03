package fr.polytech.melusine.mappers;

import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IngredientMapper {

    public IngredientResponse mapIngredientToIngredientResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .ingredientId(ingredient.getId())
                .name(ingredient.getName())
                .price(ingredient.getPrice())
                .image(ingredient.getImage())
                .build();
    }

    public List<IngredientResponse> mapIngredientsToIngredientsResponse(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(this::mapIngredientToIngredientResponse)
                .collect(Collectors.toList());
    }

}
