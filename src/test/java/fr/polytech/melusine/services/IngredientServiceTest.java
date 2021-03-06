package fr.polytech.melusine.services;

import fr.polytech.melusine.TestData;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.mappers.IngredientMapper;
import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.models.enums.IngredientType;
import fr.polytech.melusine.repositories.IngredientRepository;
import fr.polytech.melusine.repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private IngredientMapper ingredientMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private Clock clock;

    private IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        when(clock.instant()).thenReturn(TestData.INSTANT_1.toInstant());
        ingredientService = new IngredientService(ingredientRepository, ingredientMapper, productRepository, clock);
    }

    @Test
    public void createIngredient() {
        IngredientRequest request = IngredientRequest.builder()
                .name("Jambon")
                .price(10.5)
                .quantity(1)
                .build();

        ingredientService.createIngredient(request);

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo(request.getName());
        assertThat(captor.getValue().getPrice()).isEqualTo(1050L);
        assertThat(captor.getValue().getQuantity()).isEqualTo(request.getQuantity());
    }

    @Test
    public void getIngredients() {
        Ingredient ingredient = TestData.INGREDIENT_CHEESE;

        IngredientResponse response = IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .price(ingredient.getPrice())
                .quantity(ingredient.getQuantity())
                .build();

        when(ingredientRepository.findAllByTypeIsNotAndIsDeletedFalse(IngredientType.UNIQUE)).thenReturn(List.of(ingredient));
        when(ingredientMapper.mapIngredientsToIngredientsResponse(eq(List.of(ingredient)))).thenReturn(List.of(response));

        List<IngredientResponse> actual = ingredientService.getIngredientsWithoutUnique();

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(response);
    }


    @Test
    public void getIngredient() {
        String ingredientId = "ingredientId";
        Ingredient ingredient = TestData.INGREDIENT_CHEESE;

        IngredientResponse expected = IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .price(ingredient.getPrice())
                .quantity(ingredient.getQuantity())
                .build();

        when(ingredientRepository.findByIdAndIsDeletedFalse(eq(ingredientId))).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.mapIngredientToIngredientResponse(eq(ingredient))).thenReturn(expected);
        IngredientResponse actual = ingredientService.getIngredient(ingredientId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getIngredient_throwException() {
        String ingredientId = "ingredientId";

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> ingredientService.getIngredient(ingredientId))
                .withMessage("L'ingredient avec l'ID : ingredientId est introuvable");
    }

    @Test
    public void updateIngredient() {
        String ingredientId = "ingredientId";
        Ingredient ingredient = TestData.INGREDIENT_CHEESE;
        IngredientRequest request = IngredientRequest.builder()
                .name("cheese")
                .price(20.5)
                .quantity(15)
                .build();

        when(ingredientRepository.findByIdAndIsDeletedFalse(eq(ingredientId))).thenReturn(Optional.of(ingredient));

        ingredientService.updateIngredient(ingredientId, request);

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo(request.getName());
        assertThat(captor.getValue().getQuantity()).isEqualTo(request.getQuantity());
        assertThat(captor.getValue().getPrice()).isEqualTo(2050L);
    }

}
