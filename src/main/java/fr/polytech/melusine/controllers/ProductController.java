package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.ProductRequest;
import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/products", produces = "application/json; charset=UTF-8")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    public Page<ProductResponse> getProducts(@PageableDefault(size = 20, page = 0, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @GetMapping(path = "/{productId}")
    public ProductResponse getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

    @PutMapping(path = "/{productId}")
    public Product updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(productId, productRequest);
    }

}
