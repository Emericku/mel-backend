package fr.polytech.melusine.services;

import fr.polytech.melusine.components.ImageManager;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final ImageManager imageManager;
    private final Clock clock;


    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, ImageManager imageManager, Clock clock) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.imageManager = imageManager;
        this.clock = clock;
    }

    private Ingredient findIngredientById(String id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(IngredientError.NOT_FOUND, id));
    }

    public void createIngredient(IngredientRequest ingredientRequest, MultipartFile image) {
        log.debug("Create ingredient : " + ingredientRequest.getName());
        ensurePriceUpperThanZero(ingredientRequest.getPrice());
        String name = Strings.capitalize(ingredientRequest.getName().toLowerCase().trim());
        if (ingredientRepository.existsByName(ingredientRequest.getName())) {
            throw new ConflictException(IngredientError.CONFLICT, ingredientRequest.getName());
        }
        String imagePath = null;
        if (Objects.nonNull(image)) {
            imagePath = imageManager.uploadImage(image);
        }

        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .price(ingredientRequest.getPrice())
                .image(imagePath)
                .quantity(ingredientRequest.getQuantity())
                .createdAt(OffsetDateTime.now(clock))
                .updatedAt(OffsetDateTime.now(clock))
                .build();

        log.info("End of creation of an ingredient");
        ingredientRepository.save(ingredient);
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

    public Ingredient updateIngredient(IngredientRequest ingredientRequest) {
        log.debug("Update ingredient by id: {}", ingredientRequest.getId());
        ensurePriceUpperThanZero(ingredientRequest.getPrice());
        Ingredient ingredient = findIngredientById(ingredientRequest.getId());

        String name = ingredientRequest.getName().isEmpty() ? ingredient.getName() : ingredientRequest.getName();
        //String image = ingredientRequest.getImage().isEmpty() ||
        //!Objects.nonNull(ingredientRequest.getImage()) ? ingredient.getImage() : ingredientRequest.getImage();

        Ingredient updatedIngredient = ingredient.toBuilder()
                .name(name)
                .price(ingredientRequest.getPrice())
                .quantity(ingredientRequest.getQuantity())
                .build();

        log.info("End of the update of an ingredient");
        return ingredientRepository.save(updatedIngredient);
    }

    private void ensurePriceUpperThanZero(long price) {
        if (price <= 0) throw new BadRequestException(CreditError.INVALID_CREDIT, price);
    }

}
