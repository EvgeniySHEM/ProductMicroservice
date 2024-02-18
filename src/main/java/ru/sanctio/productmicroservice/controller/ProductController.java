package ru.sanctio.productmicroservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sanctio.productmicroservice.service.ProductService;
import ru.sanctio.productmicroservice.service.dto.CreateProductDto;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductDto createProductDto) {
        String productId = productService.createProduct(createProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

}
