package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.models.entities.Ingredient;
import fr.polytech.melusine.services.IngredientService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/ingredients", produces = "application/json; charset=UTF-8")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public IngredientResponse createIngredient(@RequestBody IngredientRequest ingredientRequest) {
        return ingredientService.createIngredient(ingredientRequest);
    }

    @GetMapping
    public List<IngredientResponse> getIngredients() {
        return ingredientService.getIngredients();
    }

    @GetMapping(path = "/{id}")
    public IngredientResponse getIngredient(@PathVariable String id) {
        return ingredientService.getIngredient(id);
    }

    @PutMapping(path = "/{id}")
    public IngredientResponse updateIngredient(@PathVariable String id, @RequestBody @Valid IngredientRequest ingredientRequest) {
        return ingredientService.updateIngredient(id, ingredientRequest);
    }

}
