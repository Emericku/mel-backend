package fr.polytech.melusine.services;

import fr.polytech.melusine.mappers.IngredientMapper;
import fr.polytech.melusine.repositories.IngredientRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Clock;

public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private IngredientMapper ingredientMapper;
    @Mock
    private Clock clock;

    private IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        ingredientService = new IngredientService(ingredientRepository, ingredientMapper, clock);
    }

    @Test
    public void createIngredient() {
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