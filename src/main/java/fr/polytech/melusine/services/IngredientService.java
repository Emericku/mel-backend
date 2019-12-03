package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.ConflictException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;

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

    public void createIngredient(IngredientRequest ingredientRequest) {
        log.info("Create ingredient : " + ingredientRequest.getName());
        ensurePriceUpperThanZero(ingredientRequest.getPrice());
        String name = Strings.capitalize(ingredientRequest.getName().toLowerCase().trim());
        if (ingredientRepository.existsByName(ingredientRequest.getName())) {
            throw new ConflictException(IngredientError.CONFLICT, ingredientRequest.getName());
        }

        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .price(ingredientRequest.getPrice())
                .image(ingredientRequest.getImage())
                .stock(ingredientRequest.getStock())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        ingredientRepository.save(ingredient);
    }

    public Page<IngredientResponse> getIngredients(Pageable pageable) {
        log.debug("Find ingredients by page");
        Page<Ingredient> ingredientPages = ingredientRepository.findAll(pageable);
        return ingredientPages.map(ingredientMapper::mapIngredientToIngredientResponse);
    }

    public IngredientResponse getIngredient(String ingredientId, IngredientRequest ingredientRequest) {
        log.debug("Find ingredient by UUID: {}", ingredientId);
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException(IngredientError.INVALID_INGREDIENT_UUID));
        return ingredientMapper.mapIngredientToIngredientResponse(ingredient);
    }

    public void updateIngredient(String ingredientId, IngredientRequest ingredientRequest) {
        log.debug("Update ingredient by UUID: {}", ingredientId);
        ensurePriceUpperThanZero(ingredientRequest.getPrice());
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException(IngredientError.INVALID_INGREDIENT_UUID));

        String name = ingredientRequest.getName().isEmpty() ? ingredient.getName() : ingredientRequest.getName();
        String image = ingredientRequest.getImage().isEmpty() ||
                !Objects.nonNull(ingredientRequest.getImage()) ? ingredient.getImage() : ingredientRequest.getImage();

        Ingredient updatedIngredient = ingredient.toBuilder()
                .name(name)
                .price(ingredient.getPrice())
                .image(image)
                .build();

        ingredientRepository.save(updatedIngredient);
    }

    private void ensurePriceUpperThanZero(long price) {
        if (price <= 0)
            throw new BadRequestException(CreditError.INVALID_CREDIT, price);
    }


}
