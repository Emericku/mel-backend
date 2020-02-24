package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.CreditError;
import fr.polytech.melusine.exceptions.errors.IngredientError;
import fr.polytech.melusine.mappers.IngredientMapper;
import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.repositories.IngredientRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final Clock clock;

    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, Clock clock) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.clock = clock;
    }

    public IngredientResponse createIngredient(IngredientRequest ingredientRequest) {
        log.debug("Create ingredient : " + ingredientRequest.getName());

        ensurePriceUpperThanZero(ingredientRequest.getPrice());

        String name = Strings.capitalize(ingredientRequest.getName().toLowerCase().trim());

        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .price(ingredientRequest.getPrice())
                .image(ingredientRequest.getImage())
                .quantity(ingredientRequest.getQuantity())
                .type(ingredientRequest.getType())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        Ingredient createdIngredient = ingredientRepository.save(ingredient);
        log.info("End of creation of an ingredient");

        return ingredientMapper.mapIngredientToIngredientResponse(createdIngredient);
    }

    public List<IngredientResponse> getIngredients() {
        log.debug("Find ingredients");

        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredientMapper.mapIngredientsToIngredientsResponse(ingredients);
    }

    public IngredientResponse getIngredient(String ingredientId) {
        log.debug("Find ingredient by id: {}", ingredientId);

        Ingredient ingredient = findIngredientById(ingredientId);
        return ingredientMapper.mapIngredientToIngredientResponse(ingredient);
    }

    public IngredientResponse updateIngredient(String id, IngredientRequest ingredientRequest) {
        log.debug("Update ingredient by id: {}", id);

        ensurePriceUpperThanZero(ingredientRequest.getPrice());

        Ingredient ingredientToUpdate = findIngredientById(id).toBuilder()
                .name(ingredientRequest.getName())
                .price(ingredientRequest.getPrice())
                .quantity(ingredientRequest.getQuantity())
                .type(ingredientRequest.getType())
                .build();

        Ingredient updatedIngredient = ingredientRepository.save(ingredientToUpdate);

        log.info("End of the update of an ingredient");
        return ingredientMapper.mapIngredientToIngredientResponse(updatedIngredient);
    }

    private void ensurePriceUpperThanZero(long price) {
        if (price <= 0) {
            throw new BadRequestException(CreditError.INVALID_CREDIT, price);
        }
    }

    private Ingredient findIngredientById(String id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(IngredientError.NOT_FOUND, id));
    }

}
