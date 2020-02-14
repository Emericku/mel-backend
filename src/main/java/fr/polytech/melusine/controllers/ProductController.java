package fr.polytech.melusine.controllers;

import fr.polytech.melusine.models.dtos.requests.ProductRequest;
import fr.polytech.melusine.models.dtos.responses.ProductResponse;
import fr.polytech.melusine.models.entities.Product;
import fr.polytech.melusine.services.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/products", produces = "application/json; charset=UTF-8")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductRequest productRequest, MultipartFile image) {
        return productService.createProduct(productRequest, image);
    }

    @GetMapping(path = "/{productId}")
    public ProductResponse getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

    @PutMapping(path = "/{productId}")
    public Product updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest, MultipartFile image) {
        return productService.updateProduct(productId, productRequest, image);
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

}
