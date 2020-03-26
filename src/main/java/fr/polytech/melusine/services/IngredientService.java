package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.BadRequestException;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.errors.CreditError;
import fr.polytech.melusine.exceptions.errors.IngredientError;
import fr.polytech.melusine.mappers.IngredientMapper;
import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.models.enums.IngredientType;
import fr.polytech.melusine.repositories.IngredientRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static fr.polytech.melusine.utils.AuthenticatedFinder.ensureAuthenticatedUserIsAdmin;
import static fr.polytech.melusine.utils.MoneyFormatter.formatToLong;

@Slf4j
@Service
public class IngredientService {

    public static final String PAIN_UUID = "6509e418-a12a-4a8a-b7af-8df1f7bcce00";
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final ProductRepository productRepository;
    private final Clock clock;

    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, ProductRepository productRepository, Clock clock) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.productRepository = productRepository;
        this.clock = clock;
    }

    public IngredientResponse createIngredient(IngredientRequest ingredientRequest) {
        log.debug("Create ingredient : " + ingredientRequest.getName());
        ensurePriceUpperThanZero(ingredientRequest.getPrice());

        String name = Strings.capitalize(ingredientRequest.getName().toLowerCase().trim());

        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .price(formatToLong(ingredientRequest.getPrice()))
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

    public List<IngredientResponse> getIngredientsWithoutUnique() {
        log.debug("Find ingredients");

        List<Ingredient> ingredients = ingredientRepository.findAllByTypeIsNotAndIsDeletedFalse(IngredientType.UNIQUE);
        return ingredientMapper.mapIngredientsToIngredientsResponse(ingredients);
    }

    public List<IngredientResponse> getIngredients() {
        log.debug("Find ingredients");

        List<Ingredient> ingredients = ingredientRepository.findByIsDeletedFalse();
        return ingredientMapper.mapIngredientsToIngredientsResponse(ingredients);
    }

    public IngredientResponse getIngredient(String ingredientId) {
        log.debug("Find ingredient by id: {}", ingredientId);

        Ingredient ingredient = findIngredientById(ingredientId);
        ensureIngredientIsNotDeleted(ingredientId, ingredient);
        return ingredientMapper.mapIngredientToIngredientResponse(ingredient);
    }

    private void ensureIngredientIsNotDeleted(String ingredientId, Ingredient ingredient) {
        if (ingredient.isDeleted()) {
            throw new BadRequestException(IngredientError.DELETED, ingredientId);
        }
    }

    public IngredientResponse updateIngredient(String id, IngredientRequest ingredientRequest) {
        log.debug("Update ingredient by id: {}", id);

        ensurePriceUpperThanZero(ingredientRequest.getPrice());
        Ingredient ingredientToUpdate = findIngredientById(id).toBuilder()
                .name(ingredientRequest.getName())
                .price(formatToLong(ingredientRequest.getPrice()))
                .quantity(ingredientRequest.getQuantity())
                .type(ingredientRequest.getType())
                .image(ingredientRequest.getImage())
                .build();

        List<Product> products = productRepository.findByIngredientsInAndIsDeletedFalse(List.of(ingredientToUpdate));
        products.forEach(product -> {
            List<Ingredient> updateIngredients = product.getIngredients().stream()
                    .map(productIngredient -> {
                        if (productIngredient.getId().equals(ingredientToUpdate.getId())) {
                            return ingredientToUpdate;
                        }
                        return productIngredient;
                    })
                    .collect(Collectors.toList());
            long ingredientsPrice = updateIngredients.stream()
                    .map(Ingredient::getPrice)
                    .mapToLong(Long::valueOf)
                    .sum();
            Product productAfterIngredientDeletion = product.toBuilder()
                    .ingredients(updateIngredients)
                    .price(ingredientsPrice)
                    .build();
            productRepository.save(productAfterIngredientDeletion);
        });

        Ingredient updatedIngredient = ingredientRepository.save(ingredientToUpdate);

        log.info("End of the update of an ingredient");
        return ingredientMapper.mapIngredientToIngredientResponse(updatedIngredient);
    }

    private void ensurePriceUpperThanZero(double price) {
        if (price < 0) {
            throw new BadRequestException(CreditError.INVALID_CREDIT, price);
        }
    }

    private Ingredient findIngredientById(String id) {
        return ingredientRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(IngredientError.NOT_FOUND, id));
    }

    public void deleteIngredient(String id) {
        ensureAuthenticatedUserIsAdmin();
        log.info("Start of the logic deletion of a product");
        Ingredient ingredient = findIngredientById(id);

        ensurePainIsUpdated(ingredient.getId());
        List<Product> products = productRepository.findByIngredientsInAndIsDeletedFalse(List.of(ingredient));
        products.forEach(product -> {
            List<Ingredient> ingredientAfterDeletion = product.getIngredients().stream()
                    .filter(productIngredient -> !productIngredient.getId().equals(ingredient.getId()))
                    .collect(Collectors.toList());
            Product productAfterIngredientDeletion = product.toBuilder()
                    .ingredients(ingredientAfterDeletion)
                    .build();
            productRepository.save(productAfterIngredientDeletion);
        });

        Ingredient deletedIngredient = ingredient.toBuilder()
                .isDeleted(true)
                .build();
        ingredientRepository.save(deletedIngredient);
    }

    private void ensurePainIsUpdated(String ingredientPainId) {
        if (ingredientPainId.equals(PAIN_UUID)) {
            throw new BadRequestException(IngredientError.NOT_POSSIBLE_TO_DELETE_PAIN);
        }
    }

}
