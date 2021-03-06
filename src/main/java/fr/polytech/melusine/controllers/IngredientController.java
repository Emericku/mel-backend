package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.services.IngredientService;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse createIngredient(@RequestBody IngredientRequest ingredientRequest) {
        return ingredientService.createIngredient(ingredientRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<IngredientResponse> getIngredientsWithoutUnique() {
        return ingredientService.getIngredientsWithoutUnique();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse getIngredient(@PathVariable String id) {
        return ingredientService.getIngredient(id);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse updateIngredient(@PathVariable String id, @RequestBody @Valid IngredientRequest ingredientRequest) {
        return ingredientService.updateIngredient(id, ingredientRequest);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteIngredient(@PathVariable String id) {
        ingredientService.deleteIngredient(id);
    }

    @GetMapping(path = "/all")
    @ResponseStatus(HttpStatus.OK)
    public List<IngredientResponse> getAllIngredients() {
        return ingredientService.getIngredients();
    }

}
