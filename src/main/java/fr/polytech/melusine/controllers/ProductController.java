package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.ProductRequest;
import fr.polytech.melusine.models.dtos.responses.CategoryResponse;
import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.services.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/products", produces = "application/json; charset=UTF-8")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping(path = "/{id}")
    public ProductResponse getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PutMapping(path = "/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody @Valid ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping(path = "/categories")
    public List<CategoryResponse> getCategories() {
        return productService.getCategories();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

}
