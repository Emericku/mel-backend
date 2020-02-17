package fr.polytech.melusine.services;

import fr.polytech.melusine.mappers.IngredientMapper;
import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.repositories.IngredientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceTest {

    public static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(2020, 02, 17, 13, 06, 0, 0, ZoneOffset.UTC);

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private IngredientMapper ingredientMapper;
    @Mock
    private Clock clock;

    private IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        when(clock.instant()).thenReturn(Instant.now());
        ingredientService = new IngredientService(ingredientRepository, ingredientMapper, clock);
    }

    //@Test
    public void createIngredient() {
        IngredientRequest request = IngredientRequest.builder()
                .name("Jambon")
                .price(1)
                .quantity(1)
                .image("blarf")
                .build();

        when(ingredientRepository.existsByName(eq(request.getName()))).thenReturn(false);
        when(OffsetDateTime.now()).thenReturn(OFFSET_DATE_TIME);

        ingredientService.createIngredient(request);

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo(request.getName());
        assertThat(captor.getValue().getPrice()).isEqualTo(request.getPrice());
        assertThat(captor.getValue().getQuantity()).isEqualTo(request.getQuantity());
        assertThat(captor.getValue().getImage()).isEqualTo(request.getImage());
    }

    @Test
    public void getIngredients() {
    }

    @Test
    public void getIngredient() {
    }

    @Test
    public void updateIngredient() {
    }

}