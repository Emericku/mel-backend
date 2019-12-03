package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.IngredientRequest;
import fr.polytech.melusine.models.dtos.responses.IngredientResponse;
import fr.polytech.melusine.services.IngredientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
    public Page<IngredientResponse> getIngredients(@PageableDefault(size = 20, page = 0, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        return ingredientService.getIngredients(pageable);
    }

    @GetMapping(path = "/{ingredientId}")
    public IngredientResponse getIngredient(@PathVariable String ingredientId, @RequestBody IngredientRequest ingredientRequest) {
        return ingredientService.getIngredient(ingredientId, ingredientRequest);
    }

    @PutMapping(path = "/{ingredientId}")
    public void updateIngredient(@PathVariable String ingredientId, @RequestBody IngredientRequest ingredientRequest) {
        ingredientService.updateIngredient(ingredientId, ingredientRequest);
    }

}
