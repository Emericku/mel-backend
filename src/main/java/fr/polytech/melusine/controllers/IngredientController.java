package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.services.IngredientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/ingredients", produces = "application/json; charset=UTF-8")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public void createIngredient(@RequestBody IngredientRequest ingredientRequest) {
        ingredientService.createIngredient(ingredientRequest);
    }

    @GetMapping
    public List<IngredientResponse> getIngredients() {
        return ingredientService.getIngredients();
    }

    @GetMapping(path = "/{ingredientId}")
    public IngredientResponse getIngredient(@PathVariable String ingredientId, @RequestBody IngredientRequest ingredientRequest) {
        return ingredientService.getIngredient(ingredientId);
    }

    @PutMapping(path = "/{ingredientId}")
    public Ingredient updateIngredient(@PathVariable String ingredientId, @RequestBody IngredientRequest ingredientRequest) {
        return ingredientService.updateIngredient(ingredientId, ingredientRequest);
    }

}
